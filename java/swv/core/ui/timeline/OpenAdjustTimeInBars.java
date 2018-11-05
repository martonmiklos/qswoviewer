/*    */ package com.atollic.truestudio.swv.core.ui.timeline;
/*    */ 
/*    */ import com.atollic.truestudio.swv.charts.SWVTimelineChart;
/*    */ import org.eclipse.jface.action.Action;
/*    */ import org.eclipse.swt.widgets.Event;
/*    */ import org.eclipse.swt.widgets.Listener;
/*    */ 
/*    */ public class OpenAdjustTimeInBars extends Action
/*    */   implements Listener
/*    */ {
/*    */   private static final long MIN_TIME_SLOT = 5L;
/*    */   private static final long MAX_TIME_SLOT = 480000000L;
/*    */   private SWVAbstractTimeline this_time_line;
/*    */   private double mult;
/*    */   private SWVTimelineChart current_chart;
/*    */ 
/*    */   public OpenAdjustTimeInBars(SWVAbstractTimeline current_time_line, double multiplier)
/*    */   {
/* 21 */     this.this_time_line = current_time_line;
/* 22 */     this.mult = multiplier;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try {
/* 28 */       this.current_chart = this.this_time_line.getViewer();
/* 29 */       if ((this.current_chart != null) && 
/* 30 */         (this.this_time_line.getCurrentTimeSlot() * this.mult > 5.0D) && 
/* 31 */         (this.this_time_line.getCurrentTimeSlot() * this.mult < 480000000.0D)) {
/* 32 */         this.this_time_line.multiplyCurrentTimeSlot(this.mult);
/* 33 */         this.current_chart.multiplyGraphWidthMod(this.mult);
/* 34 */         if (this.this_time_line.getCurrentClient() != null)
/*    */         {
/* 36 */           this.this_time_line.refresh();
/* 37 */           this.current_chart.adjustYAxis();
/* 38 */           this.current_chart.redraw();
/*    */         }
/*    */       }
/*    */     } catch (Exception e) {
/* 42 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void handleEvent(Event event)
/*    */   {
/* 48 */     if (event.button == 1)
/* 49 */       this.mult = 0.6666666666666666D;
/*    */     else {
/* 51 */       this.mult = 1.5D;
/*    */     }
/* 53 */     this.current_chart = this.this_time_line.getViewer();
/* 54 */     double selection = this.current_chart.getSelectionFromPixel(event.x);
/* 55 */     double diff = selection - this.current_chart.getXMin();
/* 56 */     diff *= this.mult;
/* 57 */     this.current_chart.setXMin(selection - diff);
/* 58 */     run();
/*    */   }
/*    */ 
/*    */   public void zoomOut() {
/* 62 */     this.mult = 1.5D;
/* 63 */     run();
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.timeline.OpenAdjustTimeInBars
 * JD-Core Version:    0.6.2
 */