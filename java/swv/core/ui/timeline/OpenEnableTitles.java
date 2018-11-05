/*    */ package com.atollic.truestudio.swv.core.ui.timeline;
/*    */ 
/*    */ import com.atollic.truestudio.swv.charts.SWVTimelineChart;
/*    */ import com.atollic.truestudio.swv.core.ui.Messages;
/*    */ import org.eclipse.jface.action.Action;
/*    */ import org.eclipse.jface.resource.ImageDescriptor;
/*    */ import org.eclipse.swt.graphics.Image;
/*    */ 
/*    */ public class OpenEnableTitles extends Action
/*    */ {
/*    */   private SWVAbstractTimeline this_time_line;
/*    */   private final Image actionOpenEnableTitlesImage;
/*    */   private final Image actionOpenDisableTitlesImage;
/*    */ 
/*    */   public OpenEnableTitles(SWVAbstractTimeline current_time_line, Image enableImage, Image disableImage)
/*    */   {
/* 17 */     this.this_time_line = current_time_line;
/* 18 */     this.actionOpenEnableTitlesImage = enableImage;
/* 19 */     this.actionOpenDisableTitlesImage = disableImage;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try {
/* 25 */       if (this.this_time_line.getViewer() != null)
/* 26 */         if (this.this_time_line.getViewer().isTitleEnabled()) {
/* 27 */           this.this_time_line.getViewer().enableTitle(Boolean.valueOf(false));
/* 28 */           setImageDescriptor(ImageDescriptor.createFromImage(this.actionOpenEnableTitlesImage));
/* 29 */           setToolTipText(Messages.ENABLE_TITLES);
/* 30 */           setText(Messages.ENABLE_TITLES);
/*    */         } else {
/* 32 */           this.this_time_line.getViewer().enableTitle(Boolean.valueOf(true));
/* 33 */           setImageDescriptor(ImageDescriptor.createFromImage(this.actionOpenDisableTitlesImage));
/* 34 */           setToolTipText(Messages.DISABLE_TITLES);
/* 35 */           setText(Messages.DISABLE_TITLES);
/*    */         }
/*    */     }
/*    */     catch (Exception e) {
/* 39 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.timeline.OpenEnableTitles
 * JD-Core Version:    0.6.2
 */