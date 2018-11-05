/*    */ package com.atollic.truestudio.swv.core.ui.timeline;
/*    */ 
/*    */ import com.atollic.truestudio.swv.charts.SWVTimelineChart;
/*    */ import org.eclipse.jface.action.Action;
/*    */ 
/*    */ public class OpenChangeYAxis extends Action
/*    */ {
/*    */   private SWVAbstractTimeline this_time_line;
/*    */   private double mult;
/*    */ 
/*    */   public OpenChangeYAxis(SWVAbstractTimeline current_time_line, double multiplier)
/*    */   {
/* 11 */     this.this_time_line = current_time_line;
/* 12 */     this.mult = multiplier;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try {
/* 18 */       if (this.this_time_line.getViewer() != null)
/* 19 */         this.this_time_line.getViewer().multiplyYMaxMod(this.mult);
/*    */     }
/*    */     catch (Exception e) {
/* 22 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.timeline.OpenChangeYAxis
 * JD-Core Version:    0.6.2
 */