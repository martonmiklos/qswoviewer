/*     */ package com.atollic.truestudio.swv.core.ui.config;
/*     */ 
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.debug.core.ILaunchConfiguration;
/*     */ import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ 
/*     */ public class TraceEventsWidget extends Composite
/*     */ {
/*     */   private Button btnCpi;
/*     */   private Button btnExc;
/*     */   private Button btnSleep;
/*     */   private Button btnLsu;
/*     */   private Button btnFold;
/*     */   private Button btnExetrc;
/*     */ 
/*     */   public TraceEventsWidget(Composite parent, int style)
/*     */   {
/*  35 */     super(parent, style);
/*  36 */     setLayout(new GridLayout(1, false));
/*  37 */     setLayoutData(new GridData(4, 16777216, true, true, 1, 1));
/*     */ 
/*  39 */     Group grp = new Group(this, 0);
/*  40 */     grp.setLayoutData(new GridData(4, 4, true, true, 1, 1));
/*  41 */     GridLayout gl_grpClocks = new GridLayout(2, false);
/*  42 */     grp.setLayout(gl_grpClocks);
/*  43 */     grp.setText(Messages.TraceEventsWidget_TRACE_EVENTS);
/*     */ 
/*  45 */     this.btnCpi = new Button(grp, 32);
/*  46 */     this.btnCpi.setText(Messages.TraceEventsWidget_CPI);
/*     */ 
/*  48 */     this.btnExc = new Button(grp, 32);
/*  49 */     this.btnExc.setText(Messages.TraceEventsWidget_EXC);
/*     */ 
/*  51 */     this.btnSleep = new Button(grp, 32);
/*  52 */     this.btnSleep.setText(Messages.TraceEventsWidget_SLEEP);
/*     */ 
/*  54 */     this.btnLsu = new Button(grp, 32);
/*  55 */     this.btnLsu.setText(Messages.TraceEventsWidget_LSU);
/*     */ 
/*  57 */     this.btnFold = new Button(grp, 32);
/*  58 */     this.btnFold.setLayoutData(new GridData(16384, 4, false, false, 1, 1));
/*  59 */     this.btnFold.setText(Messages.TraceEventsWidget_FOLD);
/*     */ 
/*  61 */     this.btnExetrc = new Button(grp, 32);
/*  62 */     this.btnExetrc.setText(Messages.TraceEventsWidget_EXETRC);
/*     */   }
/*     */ 
/*     */   public void init(ILaunchConfiguration config)
/*     */   {
/*     */     try
/*     */     {
/*  71 */       String attrStr = config.getAttribute("com.atollic.truestudio.swv.core.trace_events", "");
/*  72 */       String[] keyValStrs = attrStr.split(":");
/*     */ 
/*  74 */       for (String keyValStr : keyValStrs) {
/*  75 */         String[] keyVal = keyValStr.split("=");
/*  76 */         if (keyVal.length == 2) {
/*  77 */           String id = keyVal[0];
/*  78 */           String val = keyVal[1];
/*     */ 
/*  80 */           if ((val.equals("0")) || (val.equals("1"))) {
/*  81 */             boolean enabled = val.equals("1");
/*  82 */             if (id.equals("Cpi"))
/*  83 */               this.btnCpi.setSelection(enabled);
/*  84 */             else if (id.equals("Exc"))
/*  85 */               this.btnExc.setSelection(enabled);
/*  86 */             else if (id.equals("Sleep"))
/*  87 */               this.btnSleep.setSelection(enabled);
/*  88 */             else if (id.equals("Lsu"))
/*  89 */               this.btnLsu.setSelection(enabled);
/*  90 */             else if (id.equals("Fold"))
/*  91 */               this.btnFold.setSelection(enabled);
/*  92 */             else if (id.equals("Exetrc"))
/*  93 */               this.btnExetrc.setSelection(enabled);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (CoreException localCoreException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean save(ILaunchConfigurationWorkingCopy config)
/*     */   {
/* 108 */     String attrToSave = "";
/*     */ 
/* 110 */     attrToSave = attrToSave + (this.btnCpi.getSelection() ? "Cpi=1" : "Cpi=0");
/* 111 */     attrToSave = attrToSave + (this.btnExc.getSelection() ? ":Exc=1" : ":Exc=0");
/* 112 */     attrToSave = attrToSave + (this.btnSleep.getSelection() ? ":Sleep=1" : ":Sleep=0");
/* 113 */     attrToSave = attrToSave + (this.btnLsu.getSelection() ? ":Lsu=1" : ":Lsu=0");
/* 114 */     attrToSave = attrToSave + (this.btnFold.getSelection() ? ":Fold=1" : ":Fold=0");
/* 115 */     attrToSave = attrToSave + (this.btnExetrc.getSelection() ? ":Exetrc=1" : ":Exetrc=0");
/*     */ 
/* 117 */     config.setAttribute("com.atollic.truestudio.swv.core.trace_events", attrToSave);
/*     */ 
/* 119 */     return true;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.config.TraceEventsWidget
 * JD-Core Version:    0.6.2
 */