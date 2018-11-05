/*    */ package com.atollic.truestudio.swv.core.ui.preferences;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*    */ import org.eclipse.jface.preference.FieldEditorPreferencePage;
/*    */ import org.eclipse.jface.preference.IntegerFieldEditor;
/*    */ import org.eclipse.ui.IWorkbench;
/*    */ import org.eclipse.ui.IWorkbenchPreferencePage;
/*    */ 
/*    */ public class SWVPreferencePage extends FieldEditorPreferencePage
/*    */   implements IWorkbenchPreferencePage
/*    */ {
/*    */   public SWVPreferencePage()
/*    */   {
/* 26 */     super(1);
/* 27 */     setPreferenceStore(SWVPlugin.getDefault().getPreferenceStore());
/* 28 */     setDescription(Messages.SWVPreferencePage_SERIAL_WIRE_VIEWER);
/*    */   }
/*    */ 
/*    */   public void init(IWorkbench workbench)
/*    */   {
/*    */   }
/*    */ 
/*    */   protected void createFieldEditors()
/*    */   {
/* 39 */     addField(new IntegerFieldEditor("com.atollic.truestudio.swv.core.pref_buff_size", Messages.SWVPreferencePage_TRACE_BUFFER_SIZE + ":", getFieldEditorParent()));
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.preferences.SWVPreferencePage
 * JD-Core Version:    0.6.2
 */