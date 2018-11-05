/*     */ package com.atollic.truestudio.swv.core.ui.config;
/*     */ 
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.debug.core.ILaunchConfiguration;
/*     */ import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
/*     */ import org.eclipse.swt.events.ModifyEvent;
/*     */ import org.eclipse.swt.events.ModifyListener;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class DataTraceComparatorWidget extends Composite
/*     */ {
/*     */   private Text address;
/*     */   private Combo access;
/*     */   private Combo size;
/*     */   private Combo function;
/*     */   private int cmpId;
/*     */   private Button btnEnable;
/*     */ 
/*     */   public DataTraceComparatorWidget(Composite parent, int style, String name, int cmpId)
/*     */   {
/*  43 */     super(parent, style);
/*  44 */     this.cmpId = cmpId;
/*  45 */     setLayout(new GridLayout(1, false));
/*  46 */     setLayoutData(new GridData(4, 16777216, false, false, 1, 1));
/*     */ 
/*  48 */     Group grp = new Group(this, 0);
/*  49 */     grp.setLayoutData(new GridData(16384, 16777216, false, false, 1, 1));
/*  50 */     grp.setLayout(new GridLayout(2, false));
/*  51 */     grp.setLayoutData(new GridData(4, 16777216, false, false, 1, 1));
/*  52 */     grp.setText(name);
/*     */ 
/*  54 */     this.btnEnable = new Button(grp, 32);
/*  55 */     this.btnEnable.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e) {
/*  58 */         DataTraceComparatorWidget.this.enableChanged();
/*     */       }
/*     */     });
/*  61 */     this.btnEnable.setText(Messages.UI_CONFIG_ENABLE);
/*  62 */     new Label(grp, 0);
/*     */ 
/*  64 */     Label lblAddress = new Label(grp, 0);
/*  65 */     lblAddress.setLayoutData(new GridData(131072, 16777216, false, false, 1, 1));
/*  66 */     lblAddress.setText(Messages.DataTraceComparatorWidget_VAR_PER_ADDRESS + ":");
/*     */ 
/*  68 */     this.address = new Text(grp, 2048);
/*  69 */     this.address.setToolTipText(Messages.DataTraceComparatorWidget_VARIABLE_OR_ADDR_TO_TRACE);
/*     */ 
/*  71 */     this.address.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
/*     */ 
/*  73 */     this.address.setText("0x0");
/*     */ 
/*  75 */     Label lblAccess = new Label(grp, 0);
/*  76 */     lblAccess.setLayoutData(new GridData(131072, 16777216, false, false, 1, 1));
/*  77 */     lblAccess.setText(Messages.DataTraceComparatorWidget_ACCESS + ":");
/*     */ 
/*  79 */     this.access = new Combo(grp, 8);
/*  80 */     this.access.setToolTipText(Messages.DataTraceComparatorWidget_GENERATE_TRACE_EVENTS_ON_ACCESS);
/*  81 */     this.access.setItems(new String[] { "Read", "Write", "Read/Write" });
/*  82 */     this.access.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
/*  83 */     this.access.select(2);
/*     */ 
/*  85 */     Label lblSize = new Label(grp, 0);
/*  86 */     lblSize.setLayoutData(new GridData(131072, 16777216, false, false, 1, 1));
/*  87 */     lblSize.setText(Messages.DataTraceComparatorWidget_SIZE + ":");
/*     */ 
/*  89 */     this.size = new Combo(grp, 8);
/*  90 */     this.size.setToolTipText(Messages.DataTraceComparatorWidget_DATA_SIZE_TO_TRACE);
/*  91 */     this.size.setItems(new String[] { "Word", "Halfword", "Byte" });
/*  92 */     this.size.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
/*  93 */     this.size.select(0);
/*     */ 
/*  95 */     Label lblGenerate = new Label(grp, 0);
/*  96 */     lblGenerate.setLayoutData(new GridData(131072, 16777216, false, false, 1, 1));
/*  97 */     lblGenerate.setText(Messages.DataTraceComparatorWidget_GENERATE + ":");
/*     */ 
/*  99 */     this.function = new Combo(grp, 8);
/* 100 */     this.function.setToolTipText(Messages.DataTraceComparatorWidget_GENERATE_THIS_ON_ACCESS);
/* 101 */     this.function.setItems(new String[] { "Data Value", "PC", "Data Value + PC" });
/* 102 */     this.function.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
/* 103 */     this.function.select(0);
/*     */ 
/* 105 */     this.address.addModifyListener(new ModifyListener()
/*     */     {
/*     */       public void modifyText(ModifyEvent e) {
/* 108 */         if ((e.getSource() instanceof Text)) {
/* 109 */           Text txt = (Text)e.getSource();
/* 110 */           if (txt.getText().startsWith("0x"))
/* 111 */             DataTraceComparatorWidget.this.size.setEnabled(true);
/*     */           else
/* 113 */             DataTraceComparatorWidget.this.size.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public String getAddress()
/*     */   {
/* 124 */     return this.address.getText();
/*     */   }
/*     */ 
/*     */   public boolean getEnabled()
/*     */   {
/* 129 */     return this.btnEnable.getSelection();
/*     */   }
/*     */ 
/*     */   public void setAddress(String address)
/*     */   {
/* 136 */     this.address.setText(address);
/*     */   }
/*     */ 
/*     */   public String getAccess()
/*     */   {
/* 143 */     return this.access.getText();
/*     */   }
/*     */ 
/*     */   public void setAccess(String access)
/*     */   {
/* 150 */     this.access.setText(access);
/*     */   }
/*     */ 
/*     */   public String getDataSize()
/*     */   {
/* 157 */     return this.size.getText();
/*     */   }
/*     */ 
/*     */   public void setDataSize(String size)
/*     */   {
/* 164 */     this.size.setText(size);
/*     */   }
/*     */ 
/*     */   public String getFunction()
/*     */   {
/* 171 */     return this.function.getText();
/*     */   }
/*     */ 
/*     */   public void setFunction(String function)
/*     */   {
/* 178 */     this.function.setText(function);
/*     */   }
/*     */ 
/*     */   public int getCmpId()
/*     */   {
/* 185 */     return this.cmpId;
/*     */   }
/*     */ 
/*     */   public void setCmpId(int cmpId)
/*     */   {
/* 192 */     this.cmpId = cmpId;
/*     */   }
/*     */ 
/*     */   private void enableChanged() {
/* 196 */     this.address.setEnabled(this.btnEnable.getSelection());
/* 197 */     this.access.setEnabled(this.btnEnable.getSelection());
/* 198 */     this.size.setEnabled(this.btnEnable.getSelection());
/* 199 */     this.function.setEnabled(this.btnEnable.getSelection());
/* 200 */     this.size.setEnabled(this.btnEnable.getSelection());
/*     */ 
/* 202 */     if (this.btnEnable.getSelection())
/*     */     {
/* 204 */       if (!getAddress().startsWith("0x"))
/* 205 */         this.size.setEnabled(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void init(ILaunchConfiguration config)
/*     */   {
/*     */     try
/*     */     {
/* 217 */       String attrStr = config.getAttribute("com.atollic.truestudio.swv.core.datatrace_" + this.cmpId, "");
/* 218 */       String[] keyValStrs = attrStr.split(":");
/*     */ 
/* 220 */       for (String keyValStr : keyValStrs) {
/* 221 */         String[] keyVal = keyValStr.split("=");
/*     */ 
/* 223 */         if (keyVal.length == 2) {
/* 224 */           String id = keyVal[0];
/* 225 */           String val = keyVal[1];
/*     */ 
/* 228 */           if (id.equals("Address"))
/* 229 */             setAddress(val.isEmpty() ? "0x0" : val);
/* 230 */           else if (id.equals("Access"))
/* 231 */             setAccess(val);
/* 232 */           else if (id.equals("Size"))
/* 233 */             setDataSize(val);
/* 234 */           else if (id.equals("Function"))
/* 235 */             setFunction(val);
/* 236 */           else if (id.equals("Enabled")) {
/* 237 */             this.btnEnable.setSelection(val.equals("true"));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (CoreException localCoreException)
/*     */     {
/*     */     }
/*     */ 
/* 246 */     enableChanged();
/*     */   }
/*     */ 
/*     */   public boolean save(ILaunchConfigurationWorkingCopy config)
/*     */   {
/* 255 */     config.setAttribute("com.atollic.truestudio.swv.core.datatrace_" + this.cmpId, "Enabled=" + this.btnEnable.getSelection() + ":" + "Address=" + getAddress() + ":" + "Access=" + getAccess() + ":" + "Size=" + getDataSize() + ":" + "Function=" + getFunction());
/* 256 */     return true;
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.config.DataTraceComparatorWidget
 * JD-Core Version:    0.6.2
 */