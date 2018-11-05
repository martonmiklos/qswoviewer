/*    */ package com.atollic.truestudio.swv.core.ui.exception;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVClient;
/*    */ import com.atollic.truestudio.swv.core.SWVInterruptParser;
/*    */ import com.atollic.truestudio.swv.model.InterruptInfo;
/*    */ import java.text.DecimalFormat;
/*    */ import org.eclipse.jface.viewers.ColumnViewer;
/*    */ import org.eclipse.jface.viewers.StyledCellLabelProvider;
/*    */ import org.eclipse.swt.graphics.Color;
/*    */ import org.eclipse.swt.graphics.FontMetrics;
/*    */ import org.eclipse.swt.graphics.GC;
/*    */ import org.eclipse.swt.graphics.Rectangle;
/*    */ import org.eclipse.swt.widgets.Display;
/*    */ import org.eclipse.swt.widgets.Event;
/*    */ import org.eclipse.swt.widgets.Table;
/*    */ import org.eclipse.swt.widgets.TableColumn;
/*    */ import org.eclipse.swt.widgets.Widget;
/*    */ 
/*    */ class SWVExceptionStatisticViewStyledLabelProviderForTotalExceptionRun extends StyledCellLabelProvider
/*    */   implements SWVExceptionStatisticsViewLabelProviderInterface
/*    */ {
/* 24 */   private final Color colorbg = Display.getDefault().getSystemColor(32);
/*    */ 
/*    */   protected void paint(Event event, Object element)
/*    */   {
/* 28 */     String inuse = null;
/* 29 */     Object i = getViewer().getInput();
/* 30 */     if ((i instanceof SWVInterruptParser)) {
/* 31 */       SWVClient swvClient = ((SWVInterruptParser)i).getSwvClient();
/* 32 */       if ((swvClient != null) && (!swvClient.isTimestampsEnabled())) {
/* 33 */         event.gc.drawText("Timestamps not enabled", event.x, event.y, true);
/* 34 */         return;
/*    */       }
/*    */     }
/* 37 */     Widget widget = event.widget;
/* 38 */     super.paint(event, element);
/* 39 */     float f = -1.0F;
/* 40 */     if (((element instanceof InterruptInfo)) && (((InterruptInfo)element).getId() != 0)) {
/* 41 */       InterruptInfo interruptInfo = (InterruptInfo)element;
/* 42 */       if ((!interruptInfo.isTotalInfoObject()) && (interruptInfo.isAccessed()) && (!interruptInfo.getName().isEmpty()) && (interruptInfo.getTotalRun() > 0.0D)) {
/* 43 */         f = interruptInfo.getPercentTotalExceptionRun();
/* 44 */         DecimalFormat dFormat = new DecimalFormat("#.####");
/* 45 */         dFormat.setMinimumFractionDigits(4);
/* 46 */         inuse = dFormat.format(f) + "%";
/*    */ 
/* 48 */         if (f != -1.0F) {
/* 49 */           GC gc = event.gc;
/* 50 */           int width = 0;
/*    */ 
/* 52 */           if ((widget != null) && ((widget instanceof Table))) {
/* 53 */             width = (int)(((Table)widget).getColumn(5).getWidth() * f / 100.0F + 0.5F);
/*    */           } else {
/* 55 */             Rectangle rect = gc.getClipping();
/* 56 */             width = (int)(rect.width * f / 100.0F + 0.5F);
/*    */           }
/* 58 */           int x = event.x;
/* 59 */           int y = event.y;
/* 60 */           int height = event.height;
/*    */ 
/* 62 */           Color oldBackground = gc.getBackground();
/* 63 */           gc.setBackground(this.colorbg);
/* 64 */           gc.fillRectangle(x, y, width, height);
/* 65 */           gc.setBackground(oldBackground);
/*    */ 
/* 68 */           int hdiff = gc.getFontMetrics().getHeight();
/* 69 */           int wdiff = gc.getFontMetrics().getAverageCharWidth();
/* 70 */           hdiff = (int)((height - hdiff) / 2.0F + 0.5F);
/* 71 */           if (hdiff > 0) {
/* 72 */             y += hdiff;
/*    */           }
/* 74 */           gc.drawText(inuse, x + wdiff, y, true);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.exception.SWVExceptionStatisticViewStyledLabelProviderForTotalExceptionRun
 * JD-Core Version:    0.6.2
 */