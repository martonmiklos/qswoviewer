/*     */ package com.atollic.truestudio.swv.core;
/*     */ 
/*     */ import com.atollic.truestudio.common.toolchain.export.CpuCoreEnum;
/*     */ import com.atollic.truestudio.productmanager.exposed.TSProjectManager;
/*     */ import com.atollic.truestudio.swv.model.DWTExceptionEvent;
/*     */ import com.atollic.truestudio.swv.model.InterruptInfo;
/*     */ import com.atollic.truestudio.tsp.export.TargetSupportPackage;
/*     */ import com.atollic.truestudio.tsp.sfr.SfrPeripheralIRQ;
/*     */ import com.atollic.truestudio.tsp.sfr.SfrRegisterModel;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import org.eclipse.cdt.core.IAddress;
/*     */ import org.eclipse.cdt.dsf.service.DsfSession;
/*     */ import org.eclipse.cdt.utils.Addr32;
/*     */ import org.eclipse.cdt.utils.elf.Elf;
/*     */ import org.eclipse.cdt.utils.elf.Elf.Symbol;
/*     */ import org.eclipse.cdt.utils.elf.ElfHelper;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.variables.IStringVariableManager;
/*     */ import org.eclipse.core.variables.VariablesPlugin;
/*     */ import org.eclipse.debug.core.ILaunchConfiguration;
/*     */ 
/*     */ public class SWVInterruptParser
/*     */   implements ISWVEventListener
/*     */ {
/*     */   private String interuptVectorAddress;
/*     */   private ProjectInfo projectInfo;
/*     */   private SWVClient swvClient;
/*     */   private ILaunchConfiguration activeConfig;
/*  54 */   private boolean isDisposed = false;
/*     */   private ElfHelper elfhelp;
/*     */   private Elf elf;
/*     */   private SfrRegisterModel sfrRegisterModel;
/*     */   private boolean sfrIsParsed;
/*     */   private InterruptInfo[] interruptInfos;
/*  62 */   public static Integer NR_OF_INTERUPTS = Integer.valueOf(256);
/*     */   private Elf.Symbol defaultHandlerSymbol;
/*  64 */   private boolean isDefualtHandlerSearched = false;
/*     */ 
/*     */   public SWVInterruptParser(SWVClient swvClient)
/*     */   {
/* 182 */     this.elfhelp = null;
/* 183 */     this.elf = null;
/* 184 */     this.sfrIsParsed = false;
/* 185 */     this.swvClient = swvClient;
/* 186 */     this.activeConfig = (swvClient != null ? swvClient.getLaunchConfiguration() : null);
/* 187 */     handleNewTarget(new ProjectInfo(this.activeConfig));
/*     */ 
/* 189 */     SWVPlugin.getDefault().getSessionManager().addSWVEventListener(this);
/*     */   }
/*     */ 
/*     */   public void handleSWVEvent(int event)
/*     */   {
/* 198 */     if (event == 1)
/* 199 */       handleTargetResume();
/* 200 */     else if (event == 0)
/* 201 */       handleTargetSuspended();
/* 202 */     else if (event == 2)
/* 203 */       handleSWVContext();
/* 204 */     else if (event == 3)
/* 205 */       handleNoSWVContext();
/* 206 */     else if (event == 4)
/* 207 */       handleClearEvent();
/*     */     else
/* 209 */       System.out.println("Unknown SWV event sent to InterruptParser = " + event);
/*     */   }
/*     */ 
/*     */   public void handleTargetSuspended()
/*     */   {
/* 222 */     String interruptHandlerAddress = "";
/* 223 */     String function = "";
/* 224 */     if (this.elfhelp == null) {
/* 225 */       return;
/*     */     }
/* 227 */     if (this.elf == null) {
/* 228 */       this.elf = this.elfhelp.getElf();
/*     */       try {
/* 230 */         this.elf.loadSymbols();
/*     */       } catch (IOException e) {
/* 232 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 235 */     for (int interruptNumber = 1; interruptNumber < NR_OF_INTERUPTS.intValue(); interruptNumber++) {
/* 236 */       if (this.interruptInfos == null) {
/* 237 */         this.interruptInfos = new InterruptInfo[NR_OF_INTERUPTS.intValue()];
/*     */       }
/* 239 */       InterruptInfo ii = this.interruptInfos[interruptNumber];
/* 240 */       if ((ii != null) && (ii.isAccessed()) && (!ii.hasAllInfo())) {
/* 241 */         if (ii.getFunctionAddress() == null) {
/* 242 */           interruptHandlerAddress = getInteruptHandlerAddressFromGDB(interruptNumber);
/* 243 */           if (interruptHandlerAddress != null) {
/* 244 */             ii.setFunctionAddress(interruptHandlerAddress);
/*     */           }
/*     */         }
/* 247 */         if ((ii.getFunction() == null) && (ii.getFunctionAddress() != null)) {
/* 248 */           function = parseAddressToFunction(this.elf, interruptHandlerAddress);
/* 249 */           ii.setFunction(function);
/* 250 */           ii.setHasAllInfo();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleTargetResume()
/*     */   {
/* 261 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 262 */     if (sessionManager != null) {
/* 263 */       SWVClient newTraceClient = sessionManager.getClient();
/*     */ 
/* 265 */       if (newTraceClient != null)
/*     */       {
/* 267 */         if (!newTraceClient.equals(this.swvClient)) {
/* 268 */           this.swvClient = newTraceClient;
/*     */         }
/*     */       }
/*     */     }
/* 272 */     if (this.activeConfig != this.swvClient.getLaunchConfiguration()) {
/* 273 */       this.activeConfig = this.swvClient.getLaunchConfiguration();
/* 274 */       ProjectInfo newProjectInfo = new ProjectInfo(this.activeConfig);
/* 275 */       if (!newProjectInfo.equals(this.projectInfo))
/*     */       {
/* 277 */         handleNewTarget(newProjectInfo);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleSWVContext()
/*     */   {
/* 286 */     handleTargetResume();
/*     */   }
/*     */ 
/*     */   public void handleNoSWVContext()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void handleNewTarget(ProjectInfo newProjectInfo)
/*     */   {
/* 300 */     this.sfrIsParsed = false;
/* 301 */     this.isDefualtHandlerSearched = false;
/* 302 */     this.interuptVectorAddress = null;
/* 303 */     this.interruptInfos = new InterruptInfo[NR_OF_INTERUPTS.intValue()];
/* 304 */     this.projectInfo = newProjectInfo;
/* 305 */     this.sfrRegisterModel = TargetSupportPackage.getSfrRegisterModel();
/* 306 */     parseSfr();
/*     */ 
/* 308 */     if (this.elfhelp != null) {
/* 309 */       this.elfhelp.dispose();
/* 310 */       this.elfhelp = null;
/* 311 */       this.elf = null;
/*     */     }
/* 313 */     this.elfhelp = getElfHelper();
/*     */   }
/*     */ 
/*     */   public void handleClearEvent()
/*     */   {
/* 321 */     for (InterruptInfo interruptInfo : this.interruptInfos)
/* 322 */       if (interruptInfo != null)
/*     */       {
/* 325 */         interruptInfo.clear();
/*     */       }
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 332 */     if (!this.isDisposed) {
/* 333 */       SWVPlugin.getDefault().getSessionManager().removeSWVEventListener(this);
/* 334 */       if (this.elfhelp != null) {
/* 335 */         this.elfhelp.dispose();
/* 336 */         this.elfhelp = null;
/*     */       }
/* 338 */       this.interruptInfos = null;
/* 339 */       this.isDisposed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isDisposed() {
/* 344 */     return this.isDisposed;
/*     */   }
/*     */ 
/*     */   private void parseSfr()
/*     */   {
/* 352 */     if ((!this.projectInfo.isEmpty()) && (!this.sfrIsParsed))
/*     */     {
/* 354 */       Map sfrIrqInfo = this.sfrRegisterModel.getIrqInfo();
/* 355 */       if (sfrIrqInfo != null) {
/* 356 */         if (!sfrIrqInfo.isEmpty()) {
/* 357 */           for (String value : sfrIrqInfo.keySet()) {
/* 358 */             this.interruptInfos[Integer.parseInt(value)] = new InterruptInfo((SfrPeripheralIRQ)sfrIrqInfo.get(value));
/*     */           }
/*     */         }
/* 361 */         this.sfrIsParsed = true;
/*     */       }
/*     */     } else {
/* 364 */       this.sfrIsParsed = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public InterruptInfoStatisticsItem getInterruptStatistics()
/*     */   {
/* 376 */     if (isDisposed()) {
/* 377 */       return new InterruptInfoStatisticsItem(new InterruptInfo[NR_OF_INTERUPTS.intValue()]);
/*     */     }
/*     */ 
/* 380 */     return new InterruptInfoStatisticsItem(this.interruptInfos);
/*     */   }
/*     */ 
/*     */   public InterruptInfo getInterruptInfo(int interruptNumber)
/*     */   {
/* 389 */     if (interruptNumber > NR_OF_INTERUPTS.intValue() - 1) {
/* 390 */       return InterruptInfo.getDefaultInterruptInfo();
/*     */     }
/* 392 */     if (this.interruptInfos == null) {
/* 393 */       this.interruptInfos = new InterruptInfo[NR_OF_INTERUPTS.intValue()];
/*     */     }
/* 395 */     if (!this.sfrIsParsed) {
/* 396 */       parseSfr();
/*     */     }
/*     */     try
/*     */     {
/* 400 */       if (this.interruptInfos[interruptNumber] == null)
/*     */       {
/* 402 */         this.interruptInfos[interruptNumber] = new InterruptInfo(interruptNumber);
/*     */       }
/*     */     } catch (Exception e) {
/* 405 */       System.out.println("Error for interruptNumber " + interruptNumber);
/* 406 */       e.printStackTrace();
/*     */     }
/* 408 */     if (!this.interruptInfos[interruptNumber].isAccessed()) {
/* 409 */       this.interruptInfos[interruptNumber].setAccessed();
/*     */     }
/* 411 */     return this.interruptInfos[interruptNumber];
/*     */   }
/*     */ 
/*     */   public String getInterruptName(int interruptNumber)
/*     */   {
/* 420 */     InterruptInfo interruptInfo = getInterruptInfo(interruptNumber);
/* 421 */     if ((interruptInfo == null) || (interruptNumber < 0)) return Messages.SWVInterruptParser_NA;
/* 422 */     return interruptInfo.getName();
/*     */   }
/*     */ 
/*     */   public String getInterruptFunctionAddress(int interruptNumber)
/*     */   {
/* 431 */     InterruptInfo interruptInfo = getInterruptInfo(interruptNumber);
/* 432 */     if ((interruptInfo == null) || (interruptNumber < 0)) return "-1";
/* 433 */     return interruptInfo.getFunctionAddress();
/*     */   }
/*     */ 
/*     */   public String getInterruptFunction(int interruptNumber)
/*     */   {
/* 442 */     InterruptInfo interruptInfo = getInterruptInfo(interruptNumber);
/* 443 */     if ((interruptInfo == null) || (interruptNumber < 0)) return Messages.SWVInterruptParser_NA;
/* 444 */     return interruptInfo.getFunction() + "()";
/*     */   }
/*     */ 
/*     */   public String getInterruptPeripheral(int interruptNumber)
/*     */   {
/* 453 */     InterruptInfo ii = getInterruptInfo(interruptNumber);
/* 454 */     if ((ii == null) || (interruptNumber < 0)) return "";
/* 455 */     return ii.getPeripheral();
/*     */   }
/*     */ 
/*     */   public boolean interruptHasAllInfo(int interruptNumber) {
/* 459 */     InterruptInfo ii = getInterruptInfo(interruptNumber);
/* 460 */     if ((ii == null) || (interruptNumber < 0)) return false;
/* 461 */     return ii.hasAllInfo();
/*     */   }
/*     */ 
/*     */   public void addToStatistics(DWTExceptionEvent interruptEvent)
/*     */   {
/* 470 */     if ((interruptEvent.isIncludedInStatistics()) || (interruptEvent.isReturnEvent()))
/* 471 */       return;
/* 472 */     int num = interruptEvent.getExceptionNumber();
/* 473 */     InterruptInfo ii = getInterruptInfo(num);
/* 474 */     ii.updateStatistics(interruptEvent);
/*     */   }
/*     */ 
/*     */   private String getCachedInterruptVectorAddress()
/*     */   {
/* 484 */     if (this.interuptVectorAddress == null)
/*     */     {
/* 486 */       this.interuptVectorAddress = getVTORfromGDB();
/*     */     }
/* 488 */     return this.interuptVectorAddress;
/*     */   }
/*     */ 
/*     */   private String getVTORfromGDB()
/*     */   {
/* 496 */     CpuCoreEnum cpuCore = this.projectInfo.getCpuCore();
/* 497 */     switch (cpuCore) {
/*     */     case Cortex_M23:
/*     */     case Cortex_M3:
/*     */     case Cortex_M33:
/* 501 */       break;
/*     */     default:
/* 505 */       return "0";
/*     */     }
/*     */ 
/* 508 */     if (this.swvClient == null) {
/* 509 */       return null;
/*     */     }
/*     */ 
/* 512 */     DsfSession session = this.swvClient.getSession();
/* 513 */     if (session == null) {
/* 514 */       return null;
/*     */     }
/*     */ 
/* 517 */     if (this.swvClient.sessionSuspended()) {
/*     */       try {
/* 519 */         return MemoryAccess.checkoutRegisterValue("0xe000ed08", session);
/*     */       }
/*     */       catch (Exception e) {
/* 522 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 526 */     return null;
/*     */   }
/*     */ 
/*     */   private String getInteruptHandlerAddressFromGDB(int interuptNumber)
/*     */   {
/* 535 */     if (this.swvClient == null) {
/* 536 */       return null;
/*     */     }
/* 538 */     String vtor = getCachedInterruptVectorAddress();
/* 539 */     if (vtor == null) {
/* 540 */       return null;
/*     */     }
/* 542 */     int offset = interuptNumber * 4;
/* 543 */     String interuptAddress = Integer.toHexString(Integer.parseInt(vtor, 16) + offset);
/* 544 */     DsfSession session = this.swvClient.getSession();
/* 545 */     if (session == null) {
/* 546 */       return null;
/*     */     }
/* 548 */     if ((this.swvClient.sessionSuspended()) && (this.swvClient.isParsingInterrupts())) {
/* 549 */       String interruptHandlerAddress = MemoryAccess.checkoutRegisterValue("0x" + interuptAddress, session);
/* 550 */       return interruptHandlerAddress;
/*     */     }
/* 552 */     return null;
/*     */   }
/*     */ 
/*     */   private ElfHelper getElfHelper()
/*     */   {
/* 562 */     if (this.elfhelp != null) {
/* 563 */       return this.elfhelp;
/*     */     }
/* 565 */     if (this.activeConfig == null) {
/* 566 */       this.activeConfig = (this.swvClient != null ? this.swvClient.getLaunchConfiguration() : null);
/* 567 */       if (this.activeConfig == null)
/* 568 */         return null;
/*     */     }
/*     */     try
/*     */     {
/* 572 */       String programPath = this.activeConfig.getAttribute("org.eclipse.cdt.launch.PROGRAM_NAME", "");
/* 573 */       if (programPath != null) {
/* 574 */         programPath = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(programPath, true);
/*     */       }
/*     */ 
/* 577 */       String project = this.activeConfig.getAttribute("org.eclipse.cdt.launch.PROJECT_ATTR", "");
/* 578 */       IPath path = TSProjectManager.getProjectPath(project);
/* 579 */       if (path != null)
/* 580 */         programPath = path.append(programPath).toOSString();
/*     */       else {
/* 582 */         return null;
/*     */       }
/* 584 */       return new ElfHelper(programPath);
/*     */     } catch (Exception e) {
/* 586 */       e.printStackTrace();
/* 587 */     }return null;
/*     */   }
/*     */ 
/*     */   private String parseAddressToFunction(Elf elf, String address)
/*     */   {
/* 599 */     if ((address == null) || (address.equals("0"))) {
/* 600 */       return Messages.SWVInterruptParser_Not_Defined;
/*     */     }
/* 602 */     Long longAddress = Long.valueOf(Long.parseLong(address, 16));
/* 603 */     Addr32 addr32 = new Addr32(longAddress.longValue());
/* 604 */     Elf.Symbol symbol = getSymbol(addr32);
/* 605 */     return symbol.toString();
/*     */   }
/*     */ 
/*     */   public Elf.Symbol getSymbol(IAddress vma)
/*     */   {
/* 614 */     Elf.Symbol[] symbols = this.elf.getSymbols();
/* 615 */     Elf.Symbol mySymbol = null;
/* 616 */     if (symbols == null) {
/* 617 */       return null;
/*     */     }
/* 619 */     SymbolComparator symbol_comparator = new SymbolComparator();
/* 620 */     int index = 0;
/* 621 */     int ndx = Arrays.binarySearch(symbols, vma, symbol_comparator);
/* 622 */     if (ndx == -1) {
/* 623 */       return null;
/*     */     }
/* 625 */     if (ndx > 0) {
/* 626 */       index = ndx;
/*     */     } else {
/* 628 */       index = -ndx - 1;
/* 629 */       index--;
/*     */     }
/* 631 */     mySymbol = symbols[index];
/* 632 */     if (mySymbol.st_bind() != 2)
/* 633 */       return mySymbol;
/* 634 */     if ((findDefault_Handler() != null) && (vma.equals(findDefault_Handler().st_value))) {
/* 635 */       return findDefault_Handler();
/*     */     }
/*     */ 
/* 638 */     int offset = 1;
/* 639 */     int multiplier = 1;
/* 640 */     while ((mySymbol != null) && ((mySymbol.st_bind() == 2) || (mySymbol.st_value != vma)) && (offset < 150)) {
/* 641 */       int newIndex = index + offset * multiplier;
/* 642 */       if ((newIndex > 0) && (newIndex < symbols.length)) {
/* 643 */         mySymbol = symbols[newIndex];
/*     */       }
/* 645 */       multiplier *= -1;
/* 646 */       if (multiplier > 0) {
/* 647 */         offset++;
/*     */       }
/*     */     }
/*     */ 
/* 651 */     if ((mySymbol != null) && (mySymbol.st_value != vma)) {
/* 652 */       return symbols[index];
/*     */     }
/* 654 */     return mySymbol;
/*     */   }
/*     */ 
/*     */   public Elf.Symbol findDefault_Handler() {
/* 658 */     if (this.isDefualtHandlerSearched) {
/* 659 */       return this.defaultHandlerSymbol;
/*     */     }
/* 661 */     this.isDefualtHandlerSearched = true;
/* 662 */     Elf.Symbol[] symbols = this.elf.getSymbols();
/* 663 */     for (Elf.Symbol sym : symbols) {
/* 664 */       if (sym.toString().equals("Default_Handler")) {
/* 665 */         this.defaultHandlerSymbol = sym;
/* 666 */         return this.defaultHandlerSymbol;
/*     */       }
/*     */     }
/* 669 */     this.defaultHandlerSymbol = null;
/* 670 */     return this.defaultHandlerSymbol;
/*     */   }
/*     */ 
/*     */   public SWVClient getSwvClient() {
/* 674 */     return this.swvClient;
/*     */   }
/*     */ 
/*     */   public class InterruptInfoStatisticsItem
/*     */     implements Comparable<Object>
/*     */   {
/*     */     private ArrayList<InterruptInfo> existingInterrupts;
/*     */     private long numberOfInterrupts;
/*     */     private long totalTimeInInterrupt;
/*     */ 
/*     */     public InterruptInfoStatisticsItem(InterruptInfo[] interruptInfos)
/*     */     {
/* 115 */       this.numberOfInterrupts = 0L;
/* 116 */       this.totalTimeInInterrupt = 0L;
/* 117 */       this.existingInterrupts = new ArrayList();
/* 118 */       if (interruptInfos == null) {
/* 119 */         System.err.println("ERROR: Interrupt Info in Interruptinfo statistics item is null!");
/*     */       }
/*     */       else
/*     */       {
/*     */         InterruptInfo i;
/* 121 */         for (i : interruptInfos) {
/* 122 */           if ((i != null) && (i.isAccessed()) && (i.getId() != 0) && (!i.getName().isEmpty())) {
/* 123 */             this.existingInterrupts.add(i);
/* 124 */             this.numberOfInterrupts += i.getNumberOf();
/* 125 */             this.totalTimeInInterrupt += i.getTotalRun();
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 130 */       if (this.numberOfInterrupts > 0L) {
/* 131 */         InterruptInfo totalInformationAboutAllInterupts = new InterruptInfo(this.numberOfInterrupts, this.totalTimeInInterrupt);
/* 132 */         this.existingInterrupts.add(totalInformationAboutAllInterupts);
/*     */       }
/*     */ 
/* 135 */       for (InterruptInfo i : this.existingInterrupts) {
/* 136 */         i.setPercentNumberOf(this.numberOfInterrupts);
/* 137 */         i.setPercentTotalExceptionRun(this.totalTimeInInterrupt);
/*     */       }
/*     */     }
/*     */ 
/*     */     public ArrayList<InterruptInfo> getInterruptInfos()
/*     */     {
/* 146 */       return this.existingInterrupts;
/*     */     }
/*     */ 
/*     */     public float getNumberOfInterrupts()
/*     */     {
/* 153 */       return (float)this.numberOfInterrupts;
/*     */     }
/*     */ 
/*     */     public int compareTo(Object arg0)
/*     */     {
/* 158 */       if ((arg0 instanceof InterruptInfoStatisticsItem)) {
/* 159 */         if (((InterruptInfoStatisticsItem)arg0).getNumberOfInterrupts() < getNumberOfInterrupts())
/* 160 */           return -1;
/* 161 */         if (((InterruptInfoStatisticsItem)arg0).getNumberOfInterrupts() > getNumberOfInterrupts())
/* 162 */           return 1;
/*     */       }
/* 164 */       return 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   class SymbolComparator
/*     */     implements Comparator<Object>
/*     */   {
/*     */     SymbolComparator()
/*     */     {
/*     */     }
/*     */ 
/*     */     public int compare(Object o1, Object o2)
/*     */     {
/*     */       IAddress val1;
/*  83 */       if ((o1 instanceof IAddress)) {
/*  84 */         val1 = (IAddress)o1;
/*     */       }
/*     */       else
/*     */       {
/*     */         IAddress val1;
/*  85 */         if ((o1 instanceof Elf.Symbol))
/*  86 */           val1 = ((Elf.Symbol)o1).st_value;
/*     */         else
/*  88 */           return -1;
/*     */       }
/*     */       IAddress val1;
/*     */       IAddress val2;
/*  90 */       if ((o2 instanceof IAddress)) {
/*  91 */         val2 = (IAddress)o2;
/*     */       }
/*     */       else
/*     */       {
/*     */         IAddress val2;
/*  92 */         if ((o2 instanceof Elf.Symbol))
/*  93 */           val2 = ((Elf.Symbol)o2).st_value;
/*     */         else
/*  95 */           return -1;
/*     */       }
/*     */       IAddress val2;
/*  97 */       return val1.compareTo(val2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.SWVInterruptParser
 * JD-Core Version:    0.6.2
 */