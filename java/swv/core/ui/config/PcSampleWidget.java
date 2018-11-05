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
/*     */ public class PcSampleWidget extends Composite
/*     */ {
/*     */   private Button btnEnablePcSampl;
/*     */   private Combo cmbPcSamRes;
/*     */   private Label lblCyclessample;
/*     */ 
/*     */   public PcSampleWidget(Composite parent, int style)
/*     */   {
/*  35 */     super(parent, style);
/*  36 */     GridLayout layout = new GridLayout(1, false);
/*  37 */     layout.marginHeight = 0;
/*  38 */     setLayout(layout);
/*  39 */     setLayoutData(new GridData(4, 16777216, true, true, 1, 1));
/*     */ 
/*  41 */     Group grpPcSampling = new Group(this, 0);
/*  42 */     grpPcSampling.setLayoutData(new GridData(4, 4, true, false, 1, 1));
/*  43 */     grpPcSampling.setSize(232, 51);
/*  44 */     grpPcSampling.setText(Messages.PcSampleWidget_PC_SAMPLING);
/*  45 */     grpPcSampling.setLayout(new GridLayout(4, false));
/*     */ 
/*  47 */     this.btnEnablePcSampl = new Button(grpPcSampling, 32);
/*  48 */     this.btnEnablePcSampl.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e) {
/*  51 */         PcSampleWidget.this.cmbPcSamRes.setEnabled(PcSampleWidget.this.isEnabled());
/*     */       }
/*     */     });
/*  54 */     this.btnEnablePcSampl.setSize(56, 16);
/*  55 */     this.btnEnablePcSampl.setText(Messages.UI_CONFIG_ENABLE);
/*     */ 
/*  57 */     Label label_8 = new Label(grpPcSampling, 0);
/*  58 */     label_8.setText(Messages.PcSampleWidget_RESOLUTION + ":");
/*     */ 
/*  60 */     this.cmbPcSamRes = new Combo(grpPcSampling, 8);
/*  61 */     GridData gd_cmbPcSamRes = new GridData(4, 16777216, true, false, 1, 1);
/*  62 */     gd_cmbPcSamRes.minimumWidth = 70;
/*  63 */     this.cmbPcSamRes.setLayoutData(gd_cmbPcSamRes);
/*     */ 
/*  67 */     for (int i = 0; i < 16; i++) {
/*  68 */       this.cmbPcSamRes.add(64 + 64 * i);
/*     */     }
/*     */ 
/*  71 */     for (int i = 1; i < 16; i++) {
/*  72 */       this.cmbPcSamRes.add(1024 + 1024 * i);
/*     */     }
/*     */ 
/*  79 */     this.cmbPcSamRes.select(30);
/*     */ 
/*  81 */     this.lblCyclessample = new Label(grpPcSampling, 0);
/*  82 */     this.lblCyclessample.setText(Messages.PcSampleWidget_CYCLES_PER_SAMPLE);
/*     */ 
/*  85 */     setEnabled(false);
/*     */   }
/*     */ 
/*     */   public boolean isEnabled()
/*     */   {
/*  94 */     return this.btnEnablePcSampl.getSelection();
/*     */   }
/*     */ 
/*     */   public void setEnabled(boolean enabled)
/*     */   {
/* 103 */     this.btnEnablePcSampl.setSelection(enabled);
/* 104 */     this.cmbPcSamRes.setEnabled(enabled);
/*     */   }
/*     */ 
/*     */   public String getResolution()
/*     */   {
/* 112 */     return this.cmbPcSamRes.getText();
/*     */   }
/*     */ 
/*     */   public void setResolution(String resolution)
/*     */   {
/* 120 */     this.cmbPcSamRes.setText(resolution);
/*     */   }
/*     */ 
/*     */   public void init(ILaunchConfiguration config)
/*     */   {
/*     */     try
/*     */     {
/* 129 */       String attrStr = config.getAttribute("com.atollic.truestudio.swv.core.pc_sample", "");
/* 130 */       String[] keyVal = attrStr.split(":");
/* 131 */       if (keyVal.length == 2) {
/* 132 */         boolean enabled = keyVal[0].equals("1");
/* 133 */         setEnabled(enabled);
/* 134 */         setResolution(keyVal[1]);
/*     */       }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean save(ILaunchConfigurationWorkingCopy config)
/*     */   {
/* 146 */     config.setAttribute("com.atollic.truestudio.swv.core.pc_sample", (isEnabled() ? "1:" : "0:") + getResolution());
/* 147 */     return true;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.config.PcSampleWidget
 * JD-Core Version:    0.6.2
 */