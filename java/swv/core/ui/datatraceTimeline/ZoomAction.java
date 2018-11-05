/*    */ package com.atollic.truestudio.swv.core.ui.datatraceTimeline;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVInterruptParser;
/*    */ import org.eclipse.jface.action.Action;
/*    */ 
/*    */ public class ZoomAction extends Action
/*    */ {
/*    */   public static final int ZOOM_IN = 0;
/*    */   public static final int ZOOM_OUT = 1;
/*    */   private SWVInterruptParser ip;
/*    */   SWVDatatraceTimeline view;
/*    */   int direction;
/*    */ 
/*    */   public ZoomAction(SWVDatatraceTimeline view, int direction)
/*    */   {
/* 21 */     this.view = view;
/* 22 */     this.direction = direction;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 27 */     if (this.direction == 0)
/* 28 */       this.view.getChart().zoomIn();
/* 29 */     else if (this.direction == 1) {
/* 30 */       this.view.getChart().zoomOut();
/*    */     }
/*    */ 
/* 33 */     this.view.getChart().redraw();
/* 34 */     this.view.getChart().update();
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.datatraceTimeline.ZoomAction
 * JD-Core Version:    0.6.2
 */