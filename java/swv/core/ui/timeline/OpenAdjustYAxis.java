/*    */ package com.atollic.truestudio.swv.core.ui.timeline;
/*    */ 
/*    */ import com.atollic.truestudio.swv.charts.SWVTimelineChart;
/*    */ import org.eclipse.jface.action.Action;
/*    */ 
/*    */ public class OpenAdjustYAxis extends Action
/*    */ {
/*    */   private SWVAbstractTimeline this_time_line;
/*    */ 
/*    */   public OpenAdjustYAxis(SWVAbstractTimeline current_time_line)
/*    */   {
/* 10 */     this.this_time_line = current_time_line;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try {
/* 16 */       if (this.this_time_line.getViewer() != null)
/* 17 */         this.this_time_line.getViewer().adjustYAxis();
/*    */     }
/*    */     catch (Exception e) {
/* 20 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.timeline.OpenAdjustYAxis
 * JD-Core Version:    0.6.2
 */