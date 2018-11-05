/*     */ package com.atollic.truestudio.swv.core.ui;
/*     */ 
/*     */ import com.atollic.truestudio.productmanager.exposed.TSProjectManager;
/*     */ import com.atollic.truestudio.swv.core.MemoryAccess;
/*     */ import com.atollic.truestudio.swv.core.SWVClient;
/*     */ import com.atollic.truestudio.swv.core.SWVComparatorConfig;
/*     */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*     */ import com.atollic.truestudio.swv.core.SessionManager;
/*     */ import com.atollic.truestudio.swv.core.ui.datatrace.SWVDataTraceUtil;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.eclipse.cdt.dsf.service.DsfSession;
/*     */ import org.eclipse.core.commands.AbstractHandler;
/*     */ import org.eclipse.core.commands.Command;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ import org.eclipse.core.commands.ExecutionException;
/*     */ import org.eclipse.core.commands.State;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.debug.core.ILaunchConfiguration;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.eclipse.ui.handlers.HandlerUtil;
/*     */ 
/*     */ public class StartTraceHandler extends AbstractHandler
/*     */ {
/*     */   private static final String _0X = "0x";
/*     */   private static final boolean TRACE_ON = false;
/*     */   private static final String ONE = "1";
/*     */   private static final String TWO = "2";
/*     */   private static final String FOUR = "4";
/*     */ 
/*     */   public Object execute(ExecutionEvent event)
/*     */     throws ExecutionException
/*     */   {
/*  77 */     Command command = event.getCommand();
/*  78 */     HandlerUtil.toggleCommandState(command);
/*     */ 
/*  80 */     State state = command.getState("org.eclipse.ui.commands.toggleState");
/*  81 */     Boolean startTrace = (Boolean)state.getValue();
/*  82 */     SessionManager swvSessionManager = SWVPlugin.getDefault().getSessionManager();
/*     */ 
/*  84 */     SWVClient client = swvSessionManager.getClient();
/*  85 */     ILaunchConfiguration config = swvSessionManager.getActiveConfiguration();
/*     */ 
/*  87 */     if (client != null) {
/*  88 */       if (startTrace.booleanValue()) {
/*  89 */         TRACE("Start tracing");
/*  90 */         if (client.isTracing()) {
/*  91 */           TRACE("Client already tracing !!! what the heck !!!");
/*     */         }
/*  93 */         else if (enableTrace(config, client.getSession())) {
/*  94 */           client.setTracing(true);
/*  95 */           client.changeTimePrescaler();
/*     */         } else {
/*  97 */           TRACE("Failed to enable trace on target");
/*     */         }
/*     */       }
/*     */       else {
/* 101 */         TRACE("Stop tracing");
/* 102 */         if (client.isTracing()) {
/* 103 */           if (disableTrace(config, client.getSession()))
/* 104 */             client.setTracing(false);
/*     */           else
/* 106 */             TRACE("Failed to disable trace on target");
/*     */         }
/*     */         else
/* 109 */           TRACE("Client is not tracing !!! what the heck !!!");
/*     */       }
/*     */     }
/*     */     else {
/* 113 */       System.out.println("Client is NULL !!!");
/*     */     }
/*     */ 
/* 116 */     return null;
/*     */   }
/*     */ 
/*     */   private boolean enableTrace(ILaunchConfiguration config, DsfSession session)
/*     */   {
/* 133 */     ProjectInfo projectInfo = getProjectInfo(config);
/*     */ 
/* 135 */     if (projectInfo == null) {
/* 136 */       TRACE("Failed to get project information");
/* 137 */       return false;
/*     */     }
/*     */ 
/* 140 */     TRACE("Start trace capture for:\nProject: " + projectInfo.projectName + "\nVendor: " + projectInfo.vendor + "\nMCU: " + projectInfo.mcu);
/*     */     try
/*     */     {
/* 145 */       regList = getRegisterConfigsEnable(config, projectInfo, session);
/*     */     }
/*     */     catch (CoreException e)
/*     */     {
/*     */       List regList;
/* 147 */       TRACE("Failed to get register configuration");
/* 148 */       return false;
/*     */     }
/*     */     List regList;
/* 152 */     for (RegisterConfig reg : regList) {
/* 153 */       String addr = "0x" + Long.toHexString(reg.address);
/* 154 */       String val = "0x" + Long.toHexString(reg.value);
/* 155 */       if (!MemoryAccess.commitNewRegisterValue(addr, val, session)) {
/* 156 */         TRACE("Failed to write register: Address= " + addr + "Value=" + val);
/* 157 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 161 */     return true;
/*     */   }
/*     */ 
/*     */   private List<RegisterConfig> getRegisterConfigsEnable(ILaunchConfiguration config, ProjectInfo chipInfo, DsfSession session) throws CoreException
/*     */   {
/* 166 */     List regList = new ArrayList();
/*     */ 
/* 168 */     SWVClient client = SWVPlugin.getDefault().getSessionManager().getClient();
/* 169 */     if (client == null) {
/* 170 */       TRACE("Failed to get SWV client");
/* 171 */       return regList;
/*     */     }
/*     */ 
/* 175 */     regList.add(new RegisterConfig(3758157308L, 16777216L));
/*     */ 
/* 177 */     ProjectInfo projectInfo = getProjectInfo(config);
/* 178 */     if (projectInfo == null) {
/* 179 */       TRACE("Failed to get project information");
/* 180 */     } else if (projectInfo.vendor.equals("STMicroelectronics"))
/*     */     {
/* 182 */       String regValString = MemoryAccess.checkoutRegisterValue("0x" + Long.toHexString(3758366724L), session);
/* 183 */       if (regValString == null) {
/* 184 */         TRACE("Failed to read register: Address= 0x" + Long.toHexString(3758366724L));
/*     */       } else {
/* 186 */         long traceEnableValue = Long.parseLong(regValString, 16) & 0xFFFFFF3F;
/*     */ 
/* 188 */         traceEnableValue |= 32L;
/*     */ 
/* 191 */         regList.add(new RegisterConfig(3758366724L, traceEnableValue));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 196 */     regList.add(new RegisterConfig(3758358768L, 2L));
/*     */ 
/* 199 */     regList.add(new RegisterConfig(3758358544L, client.getSWOScalar()));
/*     */ 
/* 202 */     regList.add(new RegisterConfig(3758100400L, 3316436565L));
/*     */ 
/* 205 */     long itm_tcr_val = 65536L;
/* 206 */     itm_tcr_val |= 4L;
/* 207 */     itm_tcr_val |= 1L;
/* 208 */     itm_tcr_val |= 8L;
/* 209 */     String[] tcr_timestamp_setting = config.getAttribute("com.atollic.truestudio.swv.core.timestamps", "1:0").split(":");
/* 210 */     if (tcr_timestamp_setting[0].equals("1")) {
/* 211 */       itm_tcr_val |= 2L;
/* 212 */       long prescale = 0L;
/* 213 */       if (tcr_timestamp_setting[1].equals("4"))
/* 214 */         prescale = 1L;
/* 215 */       else if (tcr_timestamp_setting[1].equals("16"))
/* 216 */         prescale = 2L;
/* 217 */       else if (tcr_timestamp_setting[1].equals("64"))
/* 218 */         prescale = 3L;
/* 219 */       itm_tcr_val |= prescale << 8;
/*     */     }
/* 221 */     regList.add(new RegisterConfig(3758100096L, itm_tcr_val));
/*     */ 
/* 225 */     String[] tpr_ports = config.getAttribute("com.atollic.truestudio.swv.core.itmports_priv", "0:0:0:0").split(":");
/* 226 */     long tpr_priv_val = 0L;
/* 227 */     for (int i = 0; i < tpr_ports.length; i++) {
/* 228 */       tpr_priv_val |= (tpr_ports[i].equals("1") ? 1 << i : 0);
/*     */     }
/* 230 */     regList.add(new RegisterConfig(3758100032L, tpr_priv_val));
/*     */ 
/* 233 */     String[] stim_ports = config.getAttribute("com.atollic.truestudio.swv.core.itmports", "0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0").split(":");
/* 234 */     long itm_val = 0L;
/* 235 */     for (int i = 0; i < stim_ports.length; i++) {
/* 236 */       itm_val |= (stim_ports[i].equals("1") ? 1 << i : 0);
/*     */     }
/*     */ 
/* 239 */     regList.add(new RegisterConfig(3758099968L, itm_val));
/*     */     String symbol;
/* 243 */     for (int compCtr = 0; compCtr < 5; compCtr++)
/*     */     {
/* 246 */       boolean enabled = SWVDataTraceUtil.getComparatorEnabled((byte)compCtr, config);
/*     */ 
/* 248 */       RegisterConfig regCfgDWT_COMP_x = null;
/*     */ 
/* 250 */       if (enabled)
/*     */       {
/* 253 */         String address = SWVDataTraceUtil.getComparatorAddress((byte)compCtr, config);
/* 254 */         String access = SWVDataTraceUtil.getComparatorAccess((byte)compCtr, config);
/* 255 */         size = SWVDataTraceUtil.getComparatorSize((byte)compCtr, config);
/* 256 */         function = SWVDataTraceUtil.getComparatorFunction((byte)compCtr, config);
/*     */ 
/* 258 */         symbol = address;
/* 259 */         String type = null;
/*     */ 
/* 261 */         if ((address != null) && (access != null) && (size != null) && (function != null))
/*     */         {
/* 270 */           if (!address.startsWith("0x"))
/*     */           {
/* 272 */             String lookupError = null;
/* 273 */             type = MemoryAccess.getSymbolType(symbol, session);
/*     */ 
/* 275 */             if (type != null) {
/* 276 */               address = MemoryAccess.getSymbolAddress(symbol, session);
/* 277 */               if (address != null) {
/* 278 */                 size = MemoryAccess.getSymbolSize(symbol, session);
/*     */                 try {
/* 280 */                   int tmpSize = Integer.parseInt(size);
/* 281 */                   if ((tmpSize >= 0) && (tmpSize < 5)) break label859;
/* 282 */                   lookupError = Messages.StartTraceHandler_ONLY_1_2_AND_4_BYTES_TYPES_ALLOWED + "!";
/*     */                 }
/*     */                 catch (NumberFormatException ex) {
/* 285 */                   lookupError = Messages.StartTraceHandler_ONLY_1_2_AND_4_BYTES_TYPES_ALLOWED + "!";
/*     */                 }
/*     */               }
/*     */               else {
/* 289 */                 lookupError = Messages.StartTraceHandler_VARIABLE_NOT_FOUND + "!";
/*     */               }
/*     */             }
/*     */             else {
/* 293 */               lookupError = Messages.StartTraceHandler_VARIABLE_NOT_FOUND + "!";
/*     */             }
/*     */ 
/* 296 */             label859: if (lookupError != null)
/*     */             {
/* 298 */               MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), Messages.StartTraceHandler_DATA_TRACE, Messages.StartTraceHandler_FAILED_TO_CONFIGURE_DATA_TRACE + " \"" + symbol + "\". " + Messages.StartTraceHandler_COMPARATOR + " " + compCtr + " " + Messages.StartTraceHandler_WILL_BE_DISABLED + "\n\n" + lookupError);
/* 299 */               continue;
/*     */             }
/*     */ 
/* 302 */             if (size.equals("4"))
/* 303 */               size = "Word";
/* 304 */             else if (size.equals("2"))
/* 305 */               size = "Halfword";
/* 306 */             else if (size.equals("1"))
/* 307 */               size = "Byte";
/*     */             else {
/* 309 */               size = "Word";
/*     */             }
/*     */           }
/* 312 */           if (address.startsWith("0x")) {
/* 313 */             address = address.substring(2);
/*     */           }
/*     */           try
/*     */           {
/* 317 */             regCfgDWT_COMP_x = new RegisterConfig(3758100512L + 16 * compCtr, Long.parseLong(address, 16));
/*     */           }
/*     */           catch (NumberFormatException e) {
/* 320 */             continue;
/*     */           }
/*     */ 
/* 324 */           long mask_val = 0L;
/* 325 */           if (size.equals("Word"))
/* 326 */             mask_val = 2L;
/* 327 */           else if (size.equals("Halfword"))
/* 328 */             mask_val = 1L;
/* 329 */           else if (size.equals("Byte")) {
/* 330 */             mask_val = 0L;
/*     */           }
/*     */ 
/* 333 */           RegisterConfig regCfgDWT_MASK_x = new RegisterConfig(3758100516L + 16 * compCtr, mask_val);
/*     */ 
/* 336 */           long func_val = -1L;
/*     */ 
/* 338 */           if (access.equals("Read/Write")) {
/* 339 */             if (function.equals("PC"))
/* 340 */               func_val = 1L;
/* 341 */             else if (function.equals("Data Value"))
/* 342 */               func_val = 2L;
/* 343 */             else if (function.equals("Data Value + PC"))
/* 344 */               func_val = 3L;
/*     */           }
/* 346 */           else if (access.equals("Read")) {
/* 347 */             if (!function.equals("PC"))
/*     */             {
/* 349 */               if (function.equals("Data Value"))
/* 350 */                 func_val = 12L;
/* 351 */               else if (function.equals("Data Value + PC"))
/* 352 */                 func_val = 14L;
/*     */             }
/* 354 */           } else if ((access.equals("Write")) && 
/* 355 */             (!function.equals("PC")))
/*     */           {
/* 357 */             if (function.equals("Data Value"))
/* 358 */               func_val = 13L;
/* 359 */             else if (function.equals("Data Value + PC")) {
/* 360 */               func_val = 15L;
/*     */             }
/*     */           }
/*     */ 
/* 364 */           if (-1L != func_val)
/*     */           {
/* 369 */             RegisterConfig regCfgDWT_FUNCTION_x = new RegisterConfig(3758100520L + 16 * compCtr, func_val);
/*     */ 
/* 372 */             regList.add(regCfgDWT_COMP_x);
/* 373 */             regList.add(regCfgDWT_MASK_x);
/* 374 */             regList.add(regCfgDWT_FUNCTION_x);
/*     */ 
/* 377 */             SWVComparatorConfig cmpConfig = new SWVComparatorConfig((byte)compCtr);
/* 378 */             cmpConfig.setEnabled(true);
/* 379 */             cmpConfig.setAddress(address);
/* 380 */             cmpConfig.setSymbol(symbol);
/* 381 */             cmpConfig.setSize(size);
/* 382 */             cmpConfig.setType(type);
/*     */ 
/* 384 */             client.setComparatorConfig(cmpConfig, (byte)compCtr);
/*     */           }
/*     */         }
/*     */       } else {
/* 388 */         regCfgDWT_COMP_x = new RegisterConfig(3758100512L + 16 * compCtr, 0L);
/* 389 */         regList.add(regCfgDWT_COMP_x);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 397 */     long dwt_ctrl = 0L;
/*     */ 
/* 401 */     String attrStrTrcEvents = config.getAttribute("com.atollic.truestudio.swv.core.trace_events", "");
/* 402 */     String[] keyValStrs = attrStrTrcEvents.split(":");
/*     */ 
/* 404 */     String function = (symbol = keyValStrs).length; for (String size = 0; size < function; size++) { String keyValStr = symbol[size];
/* 405 */       String[] keyVal = keyValStr.split("=");
/* 406 */       if (keyVal.length == 2) {
/* 407 */         String id = keyVal[0];
/* 408 */         String val = keyVal[1];
/* 409 */         if (id.equals("Cpi"))
/* 410 */           dwt_ctrl |= (val.equals("1") ? 131072L : 0L);
/* 411 */         else if (id.equals("Exc"))
/* 412 */           dwt_ctrl |= (val.equals("1") ? 262144L : 0L);
/* 413 */         else if (id.equals("Sleep"))
/* 414 */           dwt_ctrl |= (val.equals("1") ? 524288L : 0L);
/* 415 */         else if (id.equals("Lsu"))
/* 416 */           dwt_ctrl |= (val.equals("1") ? 1048576L : 0L);
/* 417 */         else if (id.equals("Fold"))
/* 418 */           dwt_ctrl |= (val.equals("1") ? 2097152L : 0L);
/* 419 */         else if (id.equals("Exetrc")) {
/* 420 */           dwt_ctrl |= (val.equals("1") ? 65536L : 0L);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 426 */     String attrStrPCsamp = config.getAttribute("com.atollic.truestudio.swv.core.pc_sample", "");
/* 427 */     String[] attrPCsampKeyVal = attrStrPCsamp.split(":");
/* 428 */     if (attrPCsampKeyVal.length == 2)
/*     */     {
/* 430 */       String enabled = attrPCsampKeyVal[0];
/* 431 */       dwt_ctrl |= (enabled.equals("1") ? 4096L : 0L);
/*     */ 
/* 434 */       String cycInstrStr = attrPCsampKeyVal[1];
/*     */       try {
/* 436 */         int cycInstrVal = Integer.parseInt(cycInstrStr);
/*     */ 
/* 439 */         int tap = cycInstrVal < 2048 ? 64 : 1024;
/*     */ 
/* 441 */         if (tap == 64)
/* 442 */           dwt_ctrl |= 0L;
/*     */         else {
/* 444 */           dwt_ctrl |= 512L;
/*     */         }
/*     */ 
/* 447 */         long postCnt = (cycInstrVal - tap) / tap;
/* 448 */         dwt_ctrl |= postCnt << 5;
/*     */ 
/* 450 */         dwt_ctrl |= postCnt << 1;
/*     */ 
/* 453 */         dwt_ctrl |= 1L;
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 462 */     boolean sync_enable = config.getAttribute("com.atollic.hardwaredebug.launch.swv_wait_for_sync", false);
/* 463 */     if (sync_enable) {
/* 464 */       dwt_ctrl |= 1024L;
/*     */     }
/*     */ 
/* 468 */     regList.add(new RegisterConfig(3758100480L, dwt_ctrl));
/* 469 */     regList.add(new RegisterConfig(3758359300L, 1793L));
/*     */ 
/* 472 */     return regList;
/*     */   }
/*     */ 
/*     */   private List<RegisterConfig> getRegisterConfigsDisable(ILaunchConfiguration config, ProjectInfo chipInfo) throws CoreException {
/* 476 */     List regList = new ArrayList();
/*     */ 
/* 479 */     regList.add(new RegisterConfig(3758157308L, 0L));
/*     */ 
/* 481 */     return regList;
/*     */   }
/*     */ 
/*     */   private boolean disableTrace(ILaunchConfiguration config, Object session)
/*     */   {
/* 493 */     ProjectInfo projectInfo = getProjectInfo(config);
/*     */ 
/* 495 */     if (projectInfo == null) {
/* 496 */       TRACE("Failed to get project information");
/* 497 */       return false;
/*     */     }
/*     */ 
/* 500 */     TRACE("Stop trace capture for:\nProject: " + projectInfo.projectName + "\nVendor: " + projectInfo.vendor + "\nMCU: " + projectInfo.mcu);
/*     */     try
/*     */     {
/* 505 */       regList = getRegisterConfigsDisable(config, projectInfo);
/*     */     }
/*     */     catch (CoreException e)
/*     */     {
/*     */       List regList;
/* 507 */       TRACE("Failed to get register configuration");
/* 508 */       return false;
/*     */     }
/*     */     List regList;
/* 512 */     for (RegisterConfig reg : regList) {
/* 513 */       String addr = "0x" + Long.toHexString(reg.address);
/* 514 */       String val = "0x" + Long.toHexString(reg.value);
/* 515 */       if (!MemoryAccess.commitNewRegisterValue(addr, val, session)) {
/* 516 */         TRACE("Failed to write register: Address= " + addr + "Value=" + val);
/* 517 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 521 */     return true;
/*     */   }
/*     */ 
/*     */   private ProjectInfo getProjectInfo(ILaunchConfiguration config) {
/* 525 */     String project = "";
/*     */     try {
/* 527 */       project = config.getAttribute("org.eclipse.cdt.launch.PROJECT_ATTR", "");
/*     */     } catch (CoreException e) {
/* 529 */       return null;
/*     */     }
/*     */ 
/* 532 */     String vendor = TSProjectManager.getProjectSetting(project, "MCU_VENDOR", "");
/* 533 */     String mcu = TSProjectManager.getProjectSetting(project, "MCU", "");
/*     */ 
/* 535 */     if ((project.equals("")) || (vendor.equals("")) || (mcu.equals(""))) {
/* 536 */       return null;
/*     */     }
/*     */ 
/* 539 */     return new ProjectInfo(project, vendor, mcu);
/*     */   }
/*     */ 
/*     */   private void TRACE(String msg)
/*     */   {
/*     */   }
/*     */ 
/*     */   private class ProjectInfo
/*     */   {
/*     */     public String projectName;
/*     */     public String vendor;
/*     */     public String mcu;
/*     */ 
/*     */     public ProjectInfo(String projectName, String vendor, String mcu)
/*     */     {
/*  56 */       this.projectName = projectName;
/*  57 */       this.vendor = vendor;
/*  58 */       this.mcu = mcu;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class RegisterConfig
/*     */   {
/*     */     public long address;
/*     */     public long value;
/*     */ 
/*     */     public RegisterConfig(long address, long value) {
/*  69 */       this.address = address;
/*  70 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.StartTraceHandler
 * JD-Core Version:    0.6.2
 */