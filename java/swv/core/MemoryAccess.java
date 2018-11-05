/*     */ package com.atollic.truestudio.swv.core;
/*     */ 
/*     */ import com.atollic.truestudio.dsf.mi.commands.CLIInfoLineInfo;
/*     */ import com.atollic.truestudio.dsf.mi.commands.CLIPTypeInfo;
/*     */ import com.atollic.truestudio.dsf.mi.commands.IHardwareDebugCommandFactory;
/*     */ import java.io.PrintStream;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import org.eclipse.cdt.dsf.concurrent.DataRequestMonitor;
/*     */ import org.eclipse.cdt.dsf.concurrent.DsfExecutor;
/*     */ import org.eclipse.cdt.dsf.concurrent.Query;
/*     */ import org.eclipse.cdt.dsf.datamodel.DMContexts;
/*     */ import org.eclipse.cdt.dsf.datamodel.IDMContext;
/*     */ import org.eclipse.cdt.dsf.debug.service.IMemory.IMemoryDMContext;
/*     */ import org.eclipse.cdt.dsf.debug.service.command.ICommand;
/*     */ import org.eclipse.cdt.dsf.mi.service.IMICommandControl;
/*     */ import org.eclipse.cdt.dsf.mi.service.command.CommandFactory;
/*     */ import org.eclipse.cdt.dsf.mi.service.command.output.MIDataEvaluateExpressionInfo;
/*     */ import org.eclipse.cdt.dsf.mi.service.command.output.MIDataReadMemoryInfo;
/*     */ import org.eclipse.cdt.dsf.mi.service.command.output.MIDataWriteMemoryInfo;
/*     */ import org.eclipse.cdt.dsf.service.DsfServicesTracker;
/*     */ import org.eclipse.cdt.dsf.service.DsfSession;
/*     */ import org.eclipse.cdt.dsf.ui.viewmodel.datamodel.IDMVMContext;
/*     */ import org.eclipse.cdt.utils.Addr32;
/*     */ import org.eclipse.debug.core.model.MemoryByte;
/*     */ import org.eclipse.debug.ui.DebugUITools;
/*     */ import org.osgi.framework.Bundle;
/*     */ 
/*     */ public class MemoryAccess
/*     */ {
/*  33 */   private static DsfServicesTracker fServicesTracker = null;
/*     */ 
/*     */   private static DsfServicesTracker getServiceTracker(DsfSession session)
/*     */   {
/*  40 */     if (fServicesTracker != null) {
/*  41 */       fServicesTracker.dispose();
/*     */     }
/*     */ 
/*  44 */     fServicesTracker = new DsfServicesTracker(SWVPlugin.getDefault().getBundle().getBundleContext(), session.getId());
/*     */ 
/*  47 */     return fServicesTracker;
/*     */   }
/*     */ 
/*     */   public static IMemory.IMemoryDMContext getMemoryContext()
/*     */   {
/*  52 */     Object dbgContext = DebugUITools.getDebugContext();
/*  53 */     IDMVMContext dmvmContext = null;
/*     */ 
/*  55 */     if ((dbgContext instanceof IDMVMContext))
/*  56 */       dmvmContext = (IDMVMContext)dbgContext;
/*     */     else {
/*  58 */       return null;
/*     */     }
/*     */ 
/*  61 */     IDMContext dmContext = dmvmContext.getDMContext();
/*     */ 
/*  63 */     if (dmContext == null) {
/*  64 */       return null;
/*     */     }
/*     */ 
/*  67 */     return (IMemory.IMemoryDMContext)DMContexts.getAncestorOfType(dmContext, IMemory.IMemoryDMContext.class);
/*     */   }
/*     */ 
/*     */   public static String bytesToValStr(MemoryByte[] miBytes, boolean little)
/*     */   {
/*  73 */     if (miBytes.length == 4) {
/*  74 */       long resultLong = 0L;
/*     */ 
/*  76 */       if (little) {
/*  77 */         for (int i = 0; i < 4; i++)
/*  78 */           resultLong += ((miBytes[i].getValue() & 0xFF) << 8 * i);
/*     */       }
/*     */       else {
/*  81 */         for (int i = 0; i < 4; i++) {
/*  82 */           resultLong += ((miBytes[i].getValue() & 0xFF) << 8 * (3 - i));
/*     */         }
/*     */       }
/*     */ 
/*  86 */       return Long.toHexString(resultLong);
/*     */     }
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   private static IMICommandControl getCommandControl(DsfSession session)
/*     */   {
/*  93 */     return (IMICommandControl)getServiceTracker(session).getService(IMICommandControl.class);
/*     */   }
/*     */ 
/*     */   public static boolean commitNewRegisterValue(String address, String val, Object session)
/*     */   {
/* 106 */     if ((session instanceof DsfSession)) {
/* 107 */       DsfSession dsfSession = (DsfSession)session;
/* 108 */       DsfExecutor executor = dsfSession.getExecutor();
/*     */ 
/* 110 */       IMICommandControl commandControl = getCommandControl(dsfSession);
/*     */ 
/* 112 */       IMemory.IMemoryDMContext memoryContext = getMemoryContext();
/*     */ 
/* 114 */       new DataRequestMonitor(executor, null);
/*     */ 
/* 116 */       CommandFactory comFac = commandControl.getCommandFactory();
/*     */ 
/* 118 */       final ICommand commandWrite = comFac.createMIDataWriteMemory(memoryContext, 0L, address, 0, 4, val);
/*     */ 
/* 120 */       Query writeMemQuery = new Query()
/*     */       {
/*     */         protected void execute(DataRequestMonitor<MIDataWriteMemoryInfo> rm)
/*     */         {
/* 124 */           MemoryAccess.this.queueCommand(commandWrite, rm);
/*     */         }
/*     */       };
/*     */       try
/*     */       {
/* 129 */         executor.execute(writeMemQuery);
/*     */ 
/* 132 */         MIDataWriteMemoryInfo dataWriteInfo = (MIDataWriteMemoryInfo)writeMemQuery.get();
/*     */ 
/* 134 */         if (dataWriteInfo != null) {
/* 135 */           if (!dataWriteInfo.isError())
/*     */           {
/* 137 */             return true;
/*     */           }
/* 139 */           return false;
/*     */         }
/*     */ 
/* 142 */         return false;
/*     */       }
/*     */       catch (RejectedExecutionException ex) {
/* 145 */         return false;
/*     */       } catch (InterruptedException e) {
/* 147 */         return false;
/*     */       } catch (ExecutionException e) {
/* 149 */         return false;
/*     */       }
/*     */     }
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */   public static String checkoutRegisterValue(String address, DsfSession dsfSession)
/*     */   {
/* 163 */     DsfExecutor executor = dsfSession.getExecutor();
/*     */ 
/* 165 */     IMICommandControl commandControl = getCommandControl(dsfSession);
/* 166 */     if (commandControl == null) {
/* 167 */       System.err.println("Error: NULL commandControl");
/* 168 */       return null;
/*     */     }
/* 170 */     IMemory.IMemoryDMContext memoryContext = getMemoryContext();
/*     */ 
/* 172 */     new DataRequestMonitor(executor, null);
/*     */ 
/* 174 */     CommandFactory comFac = commandControl.getCommandFactory();
/*     */ 
/* 176 */     final ICommand commandRead = comFac.createMIDataReadMemory(memoryContext, 0L, address, 0, 4, 1, 1, Character.valueOf('y'));
/*     */ 
/* 178 */     Query readMemQuery = new Query()
/*     */     {
/*     */       protected void execute(DataRequestMonitor<MIDataReadMemoryInfo> rm)
/*     */       {
/* 182 */         MemoryAccess.this.queueCommand(commandRead, rm);
/*     */       }
/*     */     };
/*     */     try
/*     */     {
/* 187 */       executor.execute(readMemQuery);
/*     */ 
/* 190 */       MIDataReadMemoryInfo dataReadInfo = (MIDataReadMemoryInfo)readMemQuery.get();
/*     */ 
/* 192 */       if (dataReadInfo != null) {
/* 193 */         if (!dataReadInfo.isError()) {
/* 194 */           MemoryByte[] miMem = dataReadInfo.getMIMemoryBlock();
/*     */ 
/* 197 */           if (miMem.length == 4)
/*     */           {
/* 199 */             String retVal = bytesToValStr(miMem, false);
/*     */ 
/* 201 */             if (retVal != null)
/*     */             {
/* 203 */               return retVal;
/*     */             }
/*     */ 
/* 207 */             return null;
/*     */           }
/*     */ 
/* 211 */           return null;
/*     */         }
/*     */ 
/* 215 */         return null;
/*     */       }
/*     */ 
/* 219 */       return null;
/*     */     }
/*     */     catch (RejectedExecutionException ex) {
/* 222 */       return null;
/*     */     } catch (InterruptedException e) {
/* 224 */       return null; } catch (ExecutionException e) {
/*     */     }
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */   public static String getSymbolAddress(String symbol, Object session)
/*     */   {
/* 238 */     if ((session instanceof DsfSession)) {
/* 239 */       DsfSession dsfSession = (DsfSession)session;
/*     */ 
/* 241 */       DsfExecutor executor = dsfSession.getExecutor();
/*     */ 
/* 243 */       IMICommandControl commandControl = getCommandControl(dsfSession);
/*     */ 
/* 245 */       new DataRequestMonitor(executor, null);
/*     */ 
/* 247 */       CommandFactory comFac = commandControl.getCommandFactory();
/*     */       ICommand commandEval;
/*     */       final ICommand commandEval;
/* 250 */       if ((comFac instanceof IHardwareDebugCommandFactory))
/* 251 */         commandEval = ((IHardwareDebugCommandFactory)comFac).createMIDataEvaluateExpressionPure(commandControl.getContext(), "&" + symbol);
/*     */       else {
/* 253 */         commandEval = comFac.createMIDataEvaluateExpression(commandControl.getContext(), "&" + symbol);
/*     */       }
/*     */ 
/* 256 */       Query evalExprQuery = new Query()
/*     */       {
/*     */         protected void execute(DataRequestMonitor<MIDataEvaluateExpressionInfo> rm)
/*     */         {
/* 260 */           MemoryAccess.this.queueCommand(commandEval, rm);
/*     */         }
/*     */       };
/*     */       try
/*     */       {
/* 265 */         executor.execute(evalExprQuery);
/*     */ 
/* 268 */         MIDataEvaluateExpressionInfo exprInfo = (MIDataEvaluateExpressionInfo)evalExprQuery.get();
/*     */ 
/* 270 */         if (exprInfo != null) {
/* 271 */           if (!exprInfo.isError())
/*     */           {
/* 273 */             String retVal = exprInfo.getValue();
/*     */ 
/* 275 */             if (retVal != null) {
/* 276 */               if (!retVal.startsWith("0x")) break label236;
/* 277 */               int end = retVal.indexOf('"');
/* 278 */               if (end != -1) {
/* 279 */                 retVal = retVal.substring(0, end);
/*     */               }
/* 281 */               return retVal.trim();
/*     */             }
/*     */ 
/* 284 */             return null;
/*     */           }
/*     */ 
/* 288 */           return null;
/*     */         }
/*     */ 
/* 292 */         return null;
/*     */       }
/*     */       catch (RejectedExecutionException ex) {
/* 295 */         return null;
/*     */       } catch (InterruptedException e) {
/* 297 */         return null;
/*     */       } catch (ExecutionException e) {
/* 299 */         return null;
/*     */       }
/*     */     } else {
/* 302 */       return null;
/*     */     }
/*     */ 
/* 305 */     label236: return null;
/*     */   }
/*     */ 
/*     */   public static String getSymbolType(String symbol, Object session)
/*     */   {
/* 315 */     if ((session instanceof DsfSession)) {
/* 316 */       DsfSession dsfSession = (DsfSession)session;
/*     */ 
/* 318 */       DsfExecutor executor = dsfSession.getExecutor();
/*     */ 
/* 320 */       IMICommandControl commandControl = getCommandControl(dsfSession);
/*     */ 
/* 322 */       new DataRequestMonitor(executor, null);
/*     */ 
/* 324 */       IHardwareDebugCommandFactory comFac = (IHardwareDebugCommandFactory)commandControl.getCommandFactory();
/*     */ 
/* 326 */       final ICommand commandType = comFac.createCLIPType(commandControl.getContext(), symbol);
/*     */ 
/* 328 */       Query typeQuery = new Query()
/*     */       {
/*     */         protected void execute(DataRequestMonitor<CLIPTypeInfo> rm)
/*     */         {
/* 332 */           MemoryAccess.this.queueCommand(commandType, rm);
/*     */         }
/*     */       };
/*     */       try
/*     */       {
/* 337 */         executor.execute(typeQuery);
/*     */ 
/* 340 */         CLIPTypeInfo typeInfo = (CLIPTypeInfo)typeQuery.get();
/*     */ 
/* 342 */         if (typeInfo != null) {
/* 343 */           if (!typeInfo.isError())
/*     */           {
/* 345 */             String retVal = typeInfo.getType();
/*     */ 
/* 347 */             if (retVal != null) {
/* 348 */               return retVal;
/*     */             }
/* 350 */             return null;
/*     */           }
/*     */ 
/* 354 */           return null;
/*     */         }
/*     */ 
/* 358 */         return null;
/*     */       }
/*     */       catch (RejectedExecutionException ex) {
/* 361 */         return null;
/*     */       } catch (InterruptedException e) {
/* 363 */         return null;
/*     */       } catch (ExecutionException e) {
/* 365 */         return null;
/*     */       }
/*     */     }
/* 368 */     return null;
/*     */   }
/*     */ 
/*     */   public static String getSymbolSize(String symbol, Object session)
/*     */   {
/* 379 */     if ((session instanceof DsfSession)) {
/* 380 */       DsfSession dsfSession = (DsfSession)session;
/*     */ 
/* 382 */       DsfExecutor executor = dsfSession.getExecutor();
/*     */ 
/* 384 */       IMICommandControl commandControl = getCommandControl(dsfSession);
/*     */ 
/* 386 */       new DataRequestMonitor(executor, null);
/*     */ 
/* 388 */       CommandFactory comFac = commandControl.getCommandFactory();
/*     */       ICommand commandEval;
/*     */       final ICommand commandEval;
/* 391 */       if ((comFac instanceof CommandFactory))
/* 392 */         commandEval = comFac.createMIDataEvaluateExpression(commandControl.getContext(), "sizeof " + symbol);
/*     */       else {
/* 394 */         commandEval = comFac.createMIDataEvaluateExpression(commandControl.getContext(), "sizeof " + symbol);
/*     */       }
/*     */ 
/* 397 */       Query evalExprQuery = new Query()
/*     */       {
/*     */         protected void execute(DataRequestMonitor<MIDataEvaluateExpressionInfo> rm)
/*     */         {
/* 401 */           MemoryAccess.this.queueCommand(commandEval, rm);
/*     */         }
/*     */       };
/*     */       try
/*     */       {
/* 406 */         executor.execute(evalExprQuery);
/*     */ 
/* 409 */         MIDataEvaluateExpressionInfo exprInfo = (MIDataEvaluateExpressionInfo)evalExprQuery.get();
/*     */ 
/* 411 */         if (exprInfo != null) {
/* 412 */           if (!exprInfo.isError())
/*     */           {
/* 414 */             String retVal = exprInfo.getValue();
/*     */ 
/* 416 */             if (retVal != null) {
/* 417 */               return retVal;
/*     */             }
/* 419 */             return null;
/*     */           }
/*     */ 
/* 423 */           return null;
/*     */         }
/*     */ 
/* 427 */         return null;
/*     */       }
/*     */       catch (RejectedExecutionException ex) {
/* 430 */         return null;
/*     */       } catch (InterruptedException e) {
/* 432 */         return null;
/*     */       } catch (ExecutionException e) {
/* 434 */         return null;
/*     */       }
/*     */     }
/* 437 */     return null;
/*     */   }
/*     */ 
/*     */   public static CLIInfoLineInfo getLineForAddress(long address, DsfSession dsfSession)
/*     */   {
/* 448 */     DsfExecutor executor = dsfSession.getExecutor();
/*     */ 
/* 450 */     IMICommandControl commandControl = getCommandControl(dsfSession);
/*     */ 
/* 452 */     new DataRequestMonitor(executor, null);
/*     */ 
/* 454 */     IHardwareDebugCommandFactory comFac = (IHardwareDebugCommandFactory)commandControl.getCommandFactory();
/*     */ 
/* 456 */     final ICommand commandLineInfo = comFac.createCLIInfoLine(commandControl.getContext(), new Addr32(address));
/*     */ 
/* 458 */     Query typeQuery = new Query()
/*     */     {
/*     */       protected void execute(DataRequestMonitor<CLIInfoLineInfo> rm) {
/* 461 */         MemoryAccess.this.queueCommand(commandLineInfo, rm);
/*     */       }
/*     */     };
/*     */     try
/*     */     {
/* 466 */       executor.execute(typeQuery);
/*     */ 
/* 469 */       CLIInfoLineInfo lineInfo = (CLIInfoLineInfo)typeQuery.get();
/* 470 */       if (lineInfo == null) {
/* 471 */         return null;
/*     */       }
/* 473 */       if (lineInfo.isError()) {
/* 474 */         System.out.println("Error in getting address-info for address " + address + " : " + lineInfo.getErrorMsg());
/* 475 */         return null;
/*     */       }
/* 477 */       if (lineInfo.getFileName() == null) {
/* 478 */         System.out.println("Error in getting address-info for address " + address + " : " + lineInfo.getErrorMsg());
/* 479 */         return null;
/*     */       }
/* 481 */       return lineInfo; } catch (RejectedExecutionException|InterruptedException|ExecutionException e) {
/*     */     }
/* 483 */     return null;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.MemoryAccess
 * JD-Core Version:    0.6.2
 */