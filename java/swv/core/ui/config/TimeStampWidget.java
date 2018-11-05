/*     */ package com.atollic.truestudio.swv.core.ui.config;
/*     */ 
/*     */ import org.eclipse.debug.core.ILaunchConfiguration;
/*     */ import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ 
/*     */ public class TimeStampWidget extends Composite
/*     */ {
/*     */   private Button btnEnableTimeStm;
/*     */   private Combo cmbTimeStmpPre;
/*     */   private static final String ONE = "1";
/*     */   private static final String FOUR = "4";
/*     */   private static final String SIXTEEN = "16";
/*     */   private static final String SIXTYFOUR = "64";
/*     */ 
/*     */   public TimeStampWidget(Composite parent, int style)
/*     */   {
/*  39 */     super(parent, style);
/*  40 */     setLayout(new GridLayout(1, false));
/*  41 */     setLayoutData(new GridData(4, 16777216, true, true, 1, 1));
/*     */ 
/*  43 */     Group grpTimestamps = new Group(this, 0);
/*  44 */     grpTimestamps.setLayoutData(new GridData(4, 4, true, false, 1, 1));
/*  45 */     grpTimestamps.setSize(232, 51);
/*  46 */     grpTimestamps.setText(Messages.TimeStampWidget_TIMESTAMPS);
/*  47 */     grpTimestamps.setLayout(new GridLayout(3, false));
/*     */ 
/*  49 */     this.btnEnableTimeStm = new Button(grpTimestamps, 32);
/*  50 */     this.btnEnableTimeStm.setText(Messages.UI_CONFIG_ENABLE);
/*  51 */     this.btnEnableTimeStm.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e) {
/*  54 */         TimeStampWidget.this.cmbTimeStmpPre.setEnabled(TimeStampWidget.this.isEnabled());
/*     */       }
/*     */     });
/*  58 */     Label lblResolution = new Label(grpTimestamps, 0);
/*  59 */     lblResolution.setLayoutData(new GridData(131072, 16777216, false, false, 1, 1));
/*  60 */     lblResolution.setText(Messages.TimeStampWidget_PRESCALER + ":");
/*     */ 
/*  62 */     this.cmbTimeStmpPre = new Combo(grpTimestamps, 8);
/*  63 */     this.cmbTimeStmpPre.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
/*     */ 
/*  66 */     this.cmbTimeStmpPre.add("1");
/*  67 */     this.cmbTimeStmpPre.add("4");
/*  68 */     this.cmbTimeStmpPre.add("16");
/*  69 */     this.cmbTimeStmpPre.add("64");
/*  70 */     this.cmbTimeStmpPre.select(0);
/*     */ 
/*  73 */     setEnabled(true);
/*     */   }
/*     */ 
/*     */   public boolean isEnabled()
/*     */   {
/*  82 */     return this.btnEnableTimeStm.getSelection();
/*     */   }
/*     */ 
/*     */   public void setEnabled(boolean enabled)
/*     */   {
/*  91 */     this.btnEnableTimeStm.setSelection(enabled);
/*  92 */     this.cmbTimeStmpPre.setEnabled(enabled);
/*     */   }
/*     */ 
/*     */   public String getPrescaler()
/*     */   {
/* 100 */     return this.cmbTimeStmpPre.getText();
/*     */   }
/*     */ 
/*     */   public void setPrescaler(String prescaler)
/*     */   {
/* 108 */     this.cmbTimeStmpPre.setText(prescaler);
/*     */   }
/*     */ 
/*     */   public void init(ILaunchConfiguration config)
/*     */   {
/*     */     try
/*     */     {
/* 117 */       String attrStr = config.getAttribute("com.atollic.truestudio.swv.core.timestamps", "");
/* 118 */       String[] keyVal = attrStr.split(":");
/* 119 */       if (keyVal.length == 2) {
/* 120 */         boolean enabled = keyVal[0].equals("1");
/* 121 */         setEnabled(enabled);
/* 122 */         setPrescaler(keyVal[1]);
/*     */       }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean save(ILaunchConfigurationWorkingCopy config)
/*     */   {
/* 134 */     config.setAttribute("com.atollic.truestudio.swv.core.timestamps", (isEnabled() ? "1:" : "0:") + getPrescaler());
/* 135 */     return true;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.config.TimeStampWidget
 * JD-Core Version:    0.6.2
 */