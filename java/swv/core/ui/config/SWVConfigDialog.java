/*     */ package com.atollic.truestudio.swv.core.ui.config;
/*     */ 
/*     */ import com.atollic.truestudio.swv.core.MemoryAccess;
/*     */ import com.atollic.truestudio.swv.core.SWVClient;
/*     */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*     */ import com.atollic.truestudio.swv.core.SessionManager;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
/*     */ import org.eclipse.jface.dialogs.Dialog;
/*     */ import org.eclipse.jface.dialogs.IDialogConstants;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class SWVConfigDialog extends Dialog
/*     */ {
/*     */   private ILaunchConfigurationWorkingCopy fConfiguration;
/*     */   private SWVClient fClient;
/*     */   private ClockSettingsWidget clockSettings;
/*     */   private TraceEventsWidget traceEvents;
/*     */   private DataTraceComparatorWidget dataTraceCmp0;
/*     */   private DataTraceComparatorWidget dataTraceCmp1;
/*     */   private DataTraceComparatorWidget dataTraceCmp2;
/*     */   private DataTraceComparatorWidget dataTraceCmp3;
/*     */   private ITMStimulusPortsWidget itmStimulusPorts;
/*     */   private PcSampleWidget pcSampling;
/*     */   private TimeStampWidget timeStamps;
/*     */ 
/*     */   public SWVConfigDialog(Shell parentShell)
/*     */   {
/*  54 */     super(parentShell);
/*  55 */     setShellStyle(65648);
/*     */   }
/*     */ 
/*     */   protected Control createDialogArea(Composite parent)
/*     */   {
/*  64 */     GridData gd_parent = new GridData(4, 4, true, true, 1, 1);
/*  65 */     gd_parent.heightHint = -1;
/*  66 */     parent.setLayoutData(gd_parent);
/*  67 */     Composite container = (Composite)super.createDialogArea(parent);
/*     */ 
/*  70 */     container.setBackgroundMode(1);
/*     */ 
/*  72 */     super.getShell().setText(Messages.SWVConfigDialog_SWV_SETTING_FOR + this.fConfiguration);
/*  73 */     GridLayout gl_container = new GridLayout(1, false);
/*  74 */     container.setLayout(gl_container);
/*     */     try
/*     */     {
/*  78 */       Composite composite = new Composite(container, 0);
/*  79 */       composite.setLayoutData(new GridData(4, 4, false, false, 1, 1));
/*  80 */       GridLayout gl_composite = new GridLayout(3, false);
/*  81 */       gl_composite.marginWidth = 0;
/*  82 */       composite.setLayout(gl_composite);
/*     */ 
/*  85 */       this.clockSettings = new ClockSettingsWidget(composite, 0);
/*  86 */       this.clockSettings.setLayoutData(new GridData(4, 4, false, false, 1, 1));
/*  87 */       GridLayout gridLayout = (GridLayout)this.clockSettings.getLayout();
/*  88 */       gridLayout.marginHeight = 0;
/*  89 */       gridLayout.marginWidth = 0;
/*     */ 
/*  91 */       this.traceEvents = new TraceEventsWidget(composite, 0);
/*  92 */       GridLayout gridLayout_1 = (GridLayout)this.traceEvents.getLayout();
/*  93 */       gridLayout_1.marginWidth = 0;
/*  94 */       gridLayout_1.marginHeight = 0;
/*  95 */       this.traceEvents.setLayoutData(new GridData(16384, 4, false, false, 1, 1));
/*     */ 
/*  98 */       Composite composite_5 = new Composite(composite, 0);
/*  99 */       composite_5.setLayoutData(new GridData(4, 4, false, false, 1, 1));
/* 100 */       GridLayout gl_composite_5 = new GridLayout(1, false);
/* 101 */       gl_composite_5.marginHeight = 0;
/* 102 */       composite_5.setLayout(gl_composite_5);
/*     */ 
/* 104 */       this.pcSampling = new PcSampleWidget(composite_5, 0);
/* 105 */       this.timeStamps = new TimeStampWidget(composite_5, 0);
/*     */ 
/* 107 */       Group grpDataValueTrace = new Group(container, 0);
/* 108 */       grpDataValueTrace.setText(Messages.SWVConfigDialog_DATA_TRACE);
/* 109 */       grpDataValueTrace.setLayout(new GridLayout(4, false));
/* 110 */       grpDataValueTrace.setLayoutData(new GridData(4, 4, false, false, 1, 1));
/*     */ 
/* 112 */       this.dataTraceCmp0 = new DataTraceComparatorWidget(grpDataValueTrace, 0, "Comparator 0", 0);
/* 113 */       this.dataTraceCmp1 = new DataTraceComparatorWidget(grpDataValueTrace, 0, "Comparator 1", 1);
/* 114 */       this.dataTraceCmp2 = new DataTraceComparatorWidget(grpDataValueTrace, 0, "Comparator 2", 2);
/* 115 */       this.dataTraceCmp3 = new DataTraceComparatorWidget(grpDataValueTrace, 0, "Comparator 3", 3);
/*     */ 
/* 118 */       this.itmStimulusPorts = new ITMStimulusPortsWidget(container, 0);
/* 119 */       this.itmStimulusPorts.setLayoutData(new GridData(4, 4, false, false, 1, 1));
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/* 135 */     initGUI();
/*     */ 
/* 137 */     return container;
/*     */   }
/*     */ 
/*     */   private void initGUI() {
/* 141 */     this.clockSettings.init(this.fClient);
/* 142 */     this.traceEvents.init(this.fConfiguration);
/* 143 */     this.pcSampling.init(this.fConfiguration);
/* 144 */     this.timeStamps.init(this.fConfiguration);
/* 145 */     this.dataTraceCmp0.init(this.fConfiguration);
/* 146 */     this.dataTraceCmp1.init(this.fConfiguration);
/* 147 */     this.dataTraceCmp2.init(this.fConfiguration);
/* 148 */     this.dataTraceCmp3.init(this.fConfiguration);
/* 149 */     this.itmStimulusPorts.init(this.fConfiguration);
/*     */   }
/*     */ 
/*     */   protected void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 159 */     createButton(parent, 0, IDialogConstants.OK_LABEL, 
/* 160 */       true);
/* 161 */     createButton(parent, 1, 
/* 162 */       IDialogConstants.CANCEL_LABEL, false);
/*     */   }
/*     */ 
/*     */   private boolean verifyGui()
/*     */   {
/* 170 */     SWVClient client = SWVPlugin.getDefault().getSessionManager().getClient();
/* 171 */     if (client != null) {
/* 172 */       Object session = client.getSession();
/* 173 */       if (session != null)
/*     */       {
/* 176 */         if (client.sessionSuspended()) {
/* 177 */           DataTraceComparatorWidget[] comparators = { this.dataTraceCmp0, this.dataTraceCmp1, this.dataTraceCmp2, this.dataTraceCmp3 };
/* 178 */           for (int cmpId = 0; cmpId < comparators.length; cmpId++) {
/* 179 */             if (comparators[cmpId].getEnabled()) {
/* 180 */               String address = comparators[cmpId].getAddress();
/*     */ 
/* 182 */               if (!address.startsWith("0x")) {
/* 183 */                 String lookupError = null;
/*     */ 
/* 186 */                 String tmpAddress = MemoryAccess.getSymbolAddress(address, session);
/* 187 */                 if (tmpAddress != null)
/*     */                 {
/* 189 */                   String tmpSizeStr = MemoryAccess.getSymbolSize(address, session);
/*     */                   try {
/* 191 */                     int tmpSize = Integer.parseInt(tmpSizeStr);
/* 192 */                     if ((tmpSize >= 0) && (tmpSize < 5)) break label221;
/* 193 */                     lookupError = Messages.SWVConfigDialog_ONLY_1_2_AND_4_BYTES_TYPES_ALLOWED + "!";
/*     */                   }
/*     */                   catch (NumberFormatException ex) {
/* 196 */                     lookupError = Messages.SWVConfigDialog_ONLY_1_2_AND_4_BYTES_TYPES_ALLOWED + "!";
/*     */                   }
/*     */                 } else {
/* 199 */                   lookupError = Messages.SWVConfigDialog_VARIABLE_NOT_FOUND + "!";
/*     */                 }
/*     */ 
/* 203 */                 label221: if (lookupError != null) {
/* 204 */                   boolean cont = MessageDialog.openConfirm(getParentShell(), Messages.SWVConfigDialog_DATA_TRACE, Messages.SWVConfigDialog_FAILED_TO_CONFIGURE_DATA_TRACE + " \"" + address + "\".\n\n" + lookupError + "\n\n" + Messages.SWVConfigDialog_CONTINUE + "?");
/*     */ 
/* 206 */                   if (!cont) {
/* 207 */                     return false;
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 216 */     return true;
/*     */   }
/*     */ 
/*     */   protected void okPressed()
/*     */   {
/* 226 */     if (!verifyGui()) {
/* 227 */       return;
/*     */     }
/*     */ 
/* 230 */     String groupsThatFailed = "";
/*     */ 
/* 232 */     if (!this.clockSettings.save(this.fConfiguration)) {
/* 233 */       groupsThatFailed = groupsThatFailed + "Clock Settings";
/*     */     }
/*     */ 
/* 236 */     if (!this.traceEvents.save(this.fConfiguration)) {
/* 237 */       groupsThatFailed = groupsThatFailed + "\nTrace Events";
/*     */     }
/*     */ 
/* 240 */     if (!this.pcSampling.save(this.fConfiguration)) {
/* 241 */       groupsThatFailed = groupsThatFailed + "\nPC Sampling";
/*     */     }
/*     */ 
/* 244 */     if (!this.timeStamps.save(this.fConfiguration)) {
/* 245 */       groupsThatFailed = groupsThatFailed + "\nTimestamp";
/*     */     }
/*     */ 
/* 248 */     if (!this.dataTraceCmp0.save(this.fConfiguration)) {
/* 249 */       groupsThatFailed = groupsThatFailed + "\nData Trace (Comparator 0)";
/*     */     }
/*     */ 
/* 252 */     if (!this.dataTraceCmp1.save(this.fConfiguration)) {
/* 253 */       groupsThatFailed = groupsThatFailed + "\nData Trace (Comparator 1)";
/*     */     }
/*     */ 
/* 256 */     if (!this.dataTraceCmp2.save(this.fConfiguration)) {
/* 257 */       groupsThatFailed = groupsThatFailed + "\nData Trace (Comparator 2)";
/*     */     }
/*     */ 
/* 260 */     if (!this.dataTraceCmp3.save(this.fConfiguration)) {
/* 261 */       groupsThatFailed = groupsThatFailed + "\nData Trace (Comparator 3)";
/*     */     }
/*     */ 
/* 264 */     if (!this.itmStimulusPorts.save(this.fConfiguration)) {
/* 265 */       groupsThatFailed = groupsThatFailed + "\nITM Stimulus Ports";
/*     */     }
/*     */ 
/* 269 */     if (!groupsThatFailed.isEmpty())
/*     */     {
/* 271 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 275 */       this.fConfiguration.doSave();
/*     */     }
/*     */     catch (CoreException localCoreException)
/*     */     {
/*     */     }
/*     */ 
/* 281 */     super.okPressed();
/*     */   }
/*     */ 
/*     */   public void init(SWVClient client, ILaunchConfigurationWorkingCopy configuration) {
/* 285 */     this.fConfiguration = configuration;
/* 286 */     this.fClient = client;
/*     */   }
/*     */ 
/*     */   public static boolean open(Shell parent, SWVClient client, ILaunchConfigurationWorkingCopy configuration) {
/* 290 */     SWVConfigDialog dialog = new SWVConfigDialog(parent);
/* 291 */     dialog.init(client, configuration);
/* 292 */     return dialog.open() == 0;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.config.SWVConfigDialog
 * JD-Core Version:    0.6.2
 */