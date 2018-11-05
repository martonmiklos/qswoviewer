/*    */ package com.atollic.truestudio.swv.itm_trace.ui;
/*    */ 
/*    */ import org.eclipse.jface.dialogs.Dialog;
/*    */ import org.eclipse.jface.dialogs.IDialogConstants;
/*    */ import org.eclipse.swt.layout.GridData;
/*    */ import org.eclipse.swt.layout.GridLayout;
/*    */ import org.eclipse.swt.widgets.Combo;
/*    */ import org.eclipse.swt.widgets.Composite;
/*    */ import org.eclipse.swt.widgets.Control;
/*    */ import org.eclipse.swt.widgets.Label;
/*    */ import org.eclipse.swt.widgets.Shell;
/*    */ 
/*    */ public class AddPortDialog extends Dialog
/*    */ {
/*    */   SWVConsole view;
/*    */   Combo combo;
/*    */ 
/*    */   public AddPortDialog(Shell parentShell, SWVConsole view)
/*    */   {
/* 35 */     super(parentShell);
/* 36 */     this.view = view;
/*    */   }
/*    */ 
/*    */   protected Control createDialogArea(Composite parent)
/*    */   {
/* 45 */     parent.setLayoutData(new GridData(16384, 128, false, false, 1, 1));
/* 46 */     Composite container = (Composite)super.createDialogArea(parent);
/* 47 */     GridLayout gridLayout = (GridLayout)container.getLayout();
/* 48 */     gridLayout.numColumns = 2;
/* 49 */     container.setLayoutData(new GridData(16384, 128, false, false, 1, 1));
/*    */ 
/* 51 */     Label lblItmPortNumber = new Label(container, 0);
/* 52 */     lblItmPortNumber.setLayoutData(new GridData(131072, 16777216, false, false, 1, 1));
/* 53 */     lblItmPortNumber.setText(Messages.AddPortDialog_ITM_PORT_NUMBER + ":");
/*    */ 
/* 55 */     this.combo = new Combo(container, 0);
/* 56 */     this.combo.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
/*    */ 
/* 58 */     for (int i = 0; i < 32; i++)
/*    */     {
/* 60 */       if (!this.view.isPortConfigured(i)) {
/* 61 */         this.combo.add(i);
/*    */       }
/*    */     }
/*    */ 
/* 65 */     this.combo.select(0);
/* 66 */     return container;
/*    */   }
/*    */ 
/*    */   protected void configureShell(Shell newShell)
/*    */   {
/* 74 */     super.configureShell(newShell);
/* 75 */     newShell.setText(Messages.ITM_TRACE_UI_ADD_PORT);
/*    */   }
/*    */ 
/*    */   protected void createButtonsForButtonBar(Composite parent)
/*    */   {
/* 84 */     createButton(parent, 0, IDialogConstants.OK_LABEL, 
/* 85 */       true);
/* 86 */     createButton(parent, 1, 
/* 87 */       IDialogConstants.CANCEL_LABEL, false);
/*    */   }
/*    */ 
/*    */   protected void okPressed()
/*    */   {
/* 95 */     this.view.addPort(Integer.parseInt(this.combo.getText()));
/* 96 */     super.okPressed();
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.itm_trace_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.itm_trace.ui.AddPortDialog
 * JD-Core Version:    0.6.2
 */