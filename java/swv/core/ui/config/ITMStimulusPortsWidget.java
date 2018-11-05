/*     */ package com.atollic.truestudio.swv.core.ui.config;
/*     */ 
/*     */ import org.eclipse.debug.core.ILaunchConfiguration;
/*     */ import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ 
/*     */ public class ITMStimulusPortsWidget extends Composite
/*     */ {
/*     */   private static final String ZERO = "0";
/*     */   private static final String ONE = "1";
/*  31 */   private Button[] btnItmStimulusPorts = new Button[32];
/*     */   private Button btnItmPriv_24_31;
/*     */   private Button btnItmPriv_16_23;
/*     */   private Button btnItmPriv_8_15;
/*     */   private Button btnItmPriv_0_7;
/*     */ 
/*     */   public ITMStimulusPortsWidget(Composite parent, int style)
/*     */   {
/*  38 */     super(parent, style);
/*  39 */     setLayout(new GridLayout(1, false));
/*  40 */     setLayoutData(new GridData(4, 16777216, true, true, 1, 1));
/*     */ 
/*  43 */     Group grpItmStimulusPorts = new Group(this, 0);
/*  44 */     grpItmStimulusPorts.setLayoutData(new GridData(4, 4, true, false, 1, 1));
/*  45 */     grpItmStimulusPorts.setLayout(new GridLayout(1, false));
/*  46 */     grpItmStimulusPorts.setText(Messages.ITMStimulusPortsWidget_ITM_STIMULUS_PORTS);
/*     */ 
/*  48 */     Composite composite_6 = new Composite(grpItmStimulusPorts, 0);
/*  49 */     composite_6.setLayoutData(new GridData(4, 4, true, false, 1, 1));
/*  50 */     composite_6.setLayout(new GridLayout(5, false));
/*     */ 
/*  52 */     Label lblEnable = new Label(composite_6, 0);
/*  53 */     lblEnable.setText(Messages.ITMStimulusPortsWidget_ENABLE_PORT + ":");
/*     */ 
/*  55 */     Composite composite_2 = new Composite(composite_6, 0);
/*  56 */     GridLayout gl_composite_2 = new GridLayout(10, false);
/*  57 */     gl_composite_2.horizontalSpacing = 2;
/*  58 */     composite_2.setLayout(gl_composite_2);
/*     */ 
/*  60 */     Label label = new Label(composite_2, 0);
/*  61 */     label.setText("31");
/*     */ 
/*  63 */     Button btnItmPort_31 = new Button(composite_2, 32);
/*     */ 
/*  65 */     Button btnItmPort_30 = new Button(composite_2, 32);
/*  66 */     btnItmPort_30.setBounds(0, 0, 13, 16);
/*     */ 
/*  68 */     Button btnItmPort_29 = new Button(composite_2, 32);
/*     */ 
/*  70 */     Button btnItmPort_28 = new Button(composite_2, 32);
/*     */ 
/*  72 */     Button btnItmPort_27 = new Button(composite_2, 32);
/*     */ 
/*  74 */     Button btnItmPort_26 = new Button(composite_2, 32);
/*     */ 
/*  76 */     Button btnItmPort_25 = new Button(composite_2, 32);
/*     */ 
/*  78 */     Button btnItmPort_24 = new Button(composite_2, 32);
/*     */ 
/*  80 */     Label label_1 = new Label(composite_2, 0);
/*  81 */     label_1.setBounds(0, 0, 55, 15);
/*  82 */     label_1.setText("24");
/*     */ 
/*  84 */     Composite composite_1 = new Composite(composite_6, 0);
/*  85 */     composite_1.setBounds(0, 0, 161, 22);
/*  86 */     GridLayout gl_composite_1 = new GridLayout(10, false);
/*  87 */     gl_composite_1.horizontalSpacing = 2;
/*  88 */     composite_1.setLayout(gl_composite_1);
/*     */ 
/*  90 */     Label label_2 = new Label(composite_1, 0);
/*  91 */     label_2.setText("23");
/*     */ 
/*  93 */     Button btnItmPort_23 = new Button(composite_1, 32);
/*     */ 
/*  95 */     Button btnItmPort_22 = new Button(composite_1, 32);
/*     */ 
/*  97 */     Button btnItmPort_21 = new Button(composite_1, 32);
/*     */ 
/*  99 */     Button btnItmPort_20 = new Button(composite_1, 32);
/*     */ 
/* 101 */     Button btnItmPort_19 = new Button(composite_1, 32);
/*     */ 
/* 103 */     Button btnItmPort_18 = new Button(composite_1, 32);
/*     */ 
/* 105 */     Button btnItmPort_17 = new Button(composite_1, 32);
/*     */ 
/* 107 */     Button btnItmPort_16 = new Button(composite_1, 32);
/*     */ 
/* 109 */     Label label_3 = new Label(composite_1, 0);
/* 110 */     label_3.setText("16");
/*     */ 
/* 112 */     Composite composite_3 = new Composite(composite_6, 0);
/* 113 */     composite_3.setBounds(0, 0, 155, 22);
/* 114 */     GridLayout gl_composite_3 = new GridLayout(10, false);
/* 115 */     gl_composite_3.horizontalSpacing = 2;
/* 116 */     composite_3.setLayout(gl_composite_3);
/*     */ 
/* 118 */     Label label_4 = new Label(composite_3, 0);
/* 119 */     label_4.setText("15");
/*     */ 
/* 121 */     Button btnItmPort_15 = new Button(composite_3, 32);
/*     */ 
/* 123 */     Button btnItmPort_14 = new Button(composite_3, 32);
/*     */ 
/* 125 */     Button btnItmPort_13 = new Button(composite_3, 32);
/*     */ 
/* 127 */     Button btnItmPort_12 = new Button(composite_3, 32);
/*     */ 
/* 129 */     Button btnItmPort_11 = new Button(composite_3, 32);
/*     */ 
/* 131 */     Button btnItmPort_10 = new Button(composite_3, 32);
/*     */ 
/* 133 */     Button btnItmPort_9 = new Button(composite_3, 32);
/*     */ 
/* 135 */     Button btnItmPort_8 = new Button(composite_3, 32);
/*     */ 
/* 137 */     Label label_5 = new Label(composite_3, 0);
/* 138 */     label_5.setText("8");
/*     */ 
/* 140 */     Composite composite_4 = new Composite(composite_6, 0);
/* 141 */     composite_4.setBounds(0, 0, 149, 22);
/* 142 */     GridLayout gl_composite_4 = new GridLayout(10, false);
/* 143 */     gl_composite_4.horizontalSpacing = 2;
/* 144 */     composite_4.setLayout(gl_composite_4);
/*     */ 
/* 146 */     Label label_15 = new Label(composite_4, 0);
/* 147 */     label_15.setBounds(0, 0, 55, 15);
/* 148 */     label_15.setText("7");
/*     */ 
/* 150 */     Button btnItmPort_7 = new Button(composite_4, 32);
/*     */ 
/* 152 */     Button btnItmPort_6 = new Button(composite_4, 32);
/*     */ 
/* 154 */     Button btnItmPort_5 = new Button(composite_4, 32);
/*     */ 
/* 156 */     Button btnItmPort_4 = new Button(composite_4, 32);
/*     */ 
/* 158 */     Button btnItmPort_3 = new Button(composite_4, 32);
/*     */ 
/* 160 */     Button btnItmPort_2 = new Button(composite_4, 32);
/*     */ 
/* 162 */     Button btnItmPort_1 = new Button(composite_4, 32);
/*     */ 
/* 164 */     Button btnItmPort_0 = new Button(composite_4, 32);
/*     */ 
/* 167 */     this.btnItmStimulusPorts[0] = btnItmPort_0;
/* 168 */     this.btnItmStimulusPorts[1] = btnItmPort_1;
/* 169 */     this.btnItmStimulusPorts[2] = btnItmPort_2;
/* 170 */     this.btnItmStimulusPorts[3] = btnItmPort_3;
/* 171 */     this.btnItmStimulusPorts[4] = btnItmPort_4;
/* 172 */     this.btnItmStimulusPorts[5] = btnItmPort_5;
/* 173 */     this.btnItmStimulusPorts[6] = btnItmPort_6;
/* 174 */     this.btnItmStimulusPorts[7] = btnItmPort_7;
/*     */ 
/* 176 */     Label label_16 = new Label(composite_4, 0);
/* 177 */     label_16.setText("0");
/* 178 */     this.btnItmStimulusPorts[8] = btnItmPort_8;
/* 179 */     this.btnItmStimulusPorts[9] = btnItmPort_9;
/* 180 */     this.btnItmStimulusPorts[10] = btnItmPort_10;
/* 181 */     this.btnItmStimulusPorts[11] = btnItmPort_11;
/* 182 */     this.btnItmStimulusPorts[12] = btnItmPort_12;
/* 183 */     this.btnItmStimulusPorts[13] = btnItmPort_13;
/* 184 */     this.btnItmStimulusPorts[14] = btnItmPort_14;
/* 185 */     this.btnItmStimulusPorts[15] = btnItmPort_15;
/* 186 */     this.btnItmStimulusPorts[16] = btnItmPort_16;
/* 187 */     this.btnItmStimulusPorts[17] = btnItmPort_17;
/* 188 */     this.btnItmStimulusPorts[18] = btnItmPort_18;
/* 189 */     this.btnItmStimulusPorts[19] = btnItmPort_19;
/* 190 */     this.btnItmStimulusPorts[20] = btnItmPort_20;
/* 191 */     this.btnItmStimulusPorts[21] = btnItmPort_21;
/* 192 */     this.btnItmStimulusPorts[22] = btnItmPort_22;
/* 193 */     this.btnItmStimulusPorts[23] = btnItmPort_23;
/* 194 */     this.btnItmStimulusPorts[24] = btnItmPort_24;
/* 195 */     this.btnItmStimulusPorts[25] = btnItmPort_25;
/* 196 */     this.btnItmStimulusPorts[26] = btnItmPort_26;
/* 197 */     this.btnItmStimulusPorts[27] = btnItmPort_27;
/* 198 */     this.btnItmStimulusPorts[28] = btnItmPort_28;
/* 199 */     this.btnItmStimulusPorts[29] = btnItmPort_29;
/* 200 */     this.btnItmStimulusPorts[30] = btnItmPort_30;
/* 201 */     this.btnItmStimulusPorts[31] = btnItmPort_31;
/*     */ 
/* 203 */     Composite composite_7 = new Composite(grpItmStimulusPorts, 0);
/* 204 */     composite_7.setLayoutData(new GridData(4, 4, true, false, 1, 1));
/* 205 */     composite_7.setLayout(new GridLayout(5, false));
/*     */ 
/* 207 */     Label lblPriviledge = new Label(composite_7, 0);
/* 208 */     lblPriviledge.setBounds(0, 0, 55, 15);
/* 209 */     lblPriviledge.setText(Messages.ITMStimulusPortsWidget_PRIVILEDGE + ":");
/*     */ 
/* 211 */     this.btnItmPriv_24_31 = new Button(composite_7, 32);
/* 212 */     this.btnItmPriv_24_31.setText(Messages.ITMStimulusPortsWidget_PORT_31_24);
/*     */ 
/* 214 */     this.btnItmPriv_16_23 = new Button(composite_7, 32);
/* 215 */     this.btnItmPriv_16_23.setText(Messages.ITMStimulusPortsWidget_PORT_23_16);
/*     */ 
/* 217 */     this.btnItmPriv_8_15 = new Button(composite_7, 32);
/* 218 */     this.btnItmPriv_8_15.setText(Messages.ITMStimulusPortsWidget_PORT_15_8);
/*     */ 
/* 220 */     this.btnItmPriv_0_7 = new Button(composite_7, 32);
/* 221 */     this.btnItmPriv_0_7.setText(Messages.ITMStimulusPortsWidget_PORT_7_0);
/*     */   }
/*     */ 
/*     */   public void init(ILaunchConfiguration config)
/*     */   {
/*     */     try
/*     */     {
/* 232 */       String attrStrPorts = config.getAttribute("com.atollic.truestudio.swv.core.itmports", "");
/* 233 */       String attrStrPriv = config.getAttribute("com.atollic.truestudio.swv.core.itmports_priv", "");
/*     */ 
/* 235 */       String[] portVals = attrStrPorts.split(":");
/* 236 */       String[] portPriv = attrStrPriv.split(":");
/*     */ 
/* 238 */       if ((portVals.length == 32) && (portPriv.length == 4))
/*     */       {
/* 241 */         for (int i = 0; i < 32; i++) {
/* 242 */           this.btnItmStimulusPorts[i].setSelection(portVals[i].equals("1"));
/*     */         }
/*     */ 
/* 246 */         this.btnItmPriv_0_7.setSelection(portPriv[0].equals("1"));
/* 247 */         this.btnItmPriv_8_15.setSelection(portPriv[1].equals("1"));
/* 248 */         this.btnItmPriv_16_23.setSelection(portPriv[2].equals("1"));
/* 249 */         this.btnItmPriv_24_31.setSelection(portPriv[3].equals("1"));
/*     */       }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean save(ILaunchConfigurationWorkingCopy config)
/*     */   {
/* 262 */     String attrStrPorts = "";
/*     */ 
/* 264 */     for (int i = 0; i < 32; i++) {
/* 265 */       attrStrPorts = attrStrPorts + (this.btnItmStimulusPorts[i].getSelection() ? "1" : "0");
/* 266 */       if (i < 31) {
/* 267 */         attrStrPorts = attrStrPorts + ":";
/*     */       }
/*     */     }
/*     */ 
/* 271 */     String attrStrPriv = "";
/*     */ 
/* 273 */     attrStrPriv = attrStrPriv + (this.btnItmPriv_0_7.getSelection() ? "1:" : "0:");
/* 274 */     attrStrPriv = attrStrPriv + (this.btnItmPriv_8_15.getSelection() ? "1:" : "0:");
/* 275 */     attrStrPriv = attrStrPriv + (this.btnItmPriv_16_23.getSelection() ? "1:" : "0:");
/* 276 */     attrStrPriv = attrStrPriv + (this.btnItmPriv_24_31.getSelection() ? "1" : "0");
/*     */ 
/* 278 */     config.setAttribute("com.atollic.truestudio.swv.core.itmports", attrStrPorts);
/* 279 */     config.setAttribute("com.atollic.truestudio.swv.core.itmports_priv", attrStrPriv);
/*     */ 
/* 281 */     return true;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.config.ITMStimulusPortsWidget
 * JD-Core Version:    0.6.2
 */