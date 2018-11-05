/*    */ package com.atollic.truestudio.swv.itm_trace.ui;
/*    */ 
/*    */ import com.atollic.truestudio.oss.resources.SWTResourceManager;
/*    */ import com.atollic.truestudio.swv.itm_trace.Activator;
/*    */ import org.eclipse.jface.action.Action;
/*    */ import org.eclipse.jface.resource.ImageDescriptor;
/*    */ 
/*    */ public class ScrollLockAction extends Action
/*    */ {
/*    */   private SWVConsole view;
/*    */ 
/*    */   public ScrollLockAction(SWVConsole view)
/*    */   {
/* 25 */     super("", 2);
/* 26 */     this.view = view;
/* 27 */     setImageDescriptor(ImageDescriptor.createFromImage(SWTResourceManager.getImage(Activator.getDefault().getBundle(), "lock_co.gif")));
/* 28 */     setToolTipText(Messages.ScrollLockAction_SCROLL_LOCK);
/* 29 */     setChecked(false);
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 34 */     this.view.setAutoScroll(!isChecked());
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.itm_trace_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.itm_trace.ui.ScrollLockAction
 * JD-Core Version:    0.6.2
 */