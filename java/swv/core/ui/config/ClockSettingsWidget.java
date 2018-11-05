/*    */ package com.atollic.truestudio.swv.core.ui.config;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVClient;
/*    */ import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
/*    */ import org.eclipse.jface.layout.GridDataFactory;
/*    */ import org.eclipse.jface.layout.GridLayoutFactory;
/*    */ import org.eclipse.swt.widgets.Composite;
/*    */ import org.eclipse.swt.widgets.Group;
/*    */ import org.eclipse.swt.widgets.Label;
/*    */ import org.eclipse.swt.widgets.Text;
/*    */ 
/*    */ public class ClockSettingsWidget extends Composite
/*    */ {
/*    */   private Text fCoreClock;
/*    */   private Text fClockPrescaler;
/*    */   private Text fSwoClock;
/*    */ 
/*    */   public ClockSettingsWidget(Composite parent, int style)
/*    */   {
/* 31 */     super(parent, style);
/*    */ 
/* 33 */     GridLayoutFactory.fillDefaults().applyTo(this);
/* 34 */     GridDataFactory.fillDefaults().grab(true, true).align(4, 16777216).applyTo(this);
/*    */ 
/* 36 */     Group grp = new Group(this, 0);
/* 37 */     GridDataFactory.fillDefaults().grab(true, true).applyTo(grp);
/* 38 */     GridLayoutFactory.swtDefaults().numColumns(3).applyTo(grp);
/* 39 */     grp.setText(Messages.ClockSettingsWidget_CLOCK_SETTINGS);
/*    */ 
/* 41 */     Label lblCoreClock = new Label(grp, 0);
/* 42 */     GridDataFactory.swtDefaults().applyTo(lblCoreClock);
/* 43 */     lblCoreClock.setText(Messages.ClockSettingsWidget_CORE_CLOCK + ":");
/*    */ 
/* 45 */     this.fCoreClock = new Text(grp, 2056);
/* 46 */     GridDataFactory.swtDefaults().align(4, 16777216).applyTo(this.fCoreClock);
/* 47 */     this.fCoreClock.setEnabled(false);
/*    */ 
/* 49 */     Label lblMhz = new Label(grp, 0);
/* 50 */     GridDataFactory.swtDefaults().applyTo(lblMhz);
/* 51 */     lblMhz.setText(Messages.ClockSettingsWidget_MHz);
/*    */ 
/* 53 */     Label lblSwoClockPrescaler = new Label(grp, 0);
/* 54 */     lblSwoClockPrescaler.setText(Messages.ClockSettingsWidget_CLOCK_PRESCALER + ":");
/*    */ 
/* 56 */     this.fClockPrescaler = new Text(grp, 2056);
/* 57 */     GridDataFactory.swtDefaults().align(4, 16777216).applyTo(this.fClockPrescaler);
/* 58 */     this.fClockPrescaler.setEnabled(false);
/* 59 */     GridDataFactory.swtDefaults().applyTo(new Label(grp, 0));
/*    */ 
/* 61 */     Label lblSwoClock = new Label(grp, 0);
/* 62 */     GridDataFactory.swtDefaults().applyTo(lblSwoClock);
/* 63 */     lblSwoClock.setText(Messages.ClockSettingsWidget_SWO_CLOCK + ":");
/*    */ 
/* 65 */     this.fSwoClock = new Text(grp, 2048);
/* 66 */     GridDataFactory.swtDefaults().align(4, 16777216).applyTo(this.fSwoClock);
/* 67 */     this.fSwoClock.setEnabled(false);
/* 68 */     this.fSwoClock.setEditable(false);
/*    */ 
/* 70 */     Label lblKhz = new Label(grp, 0);
/* 71 */     GridDataFactory.swtDefaults().applyTo(lblKhz);
/* 72 */     lblKhz.setText(Messages.ClockSettingsWidget_kHz);
/*    */   }
/*    */ 
/*    */   public void init(SWVClient client)
/*    */   {
/* 80 */     int coreClock = client.getSWVCoreClock();
/* 81 */     int traceDiv = client.getSWVTraceDiv();
/* 82 */     int swoClock = client.getSWOClock();
/*    */ 
/* 84 */     this.fCoreClock.setText(Integer.toString(coreClock / 1000000));
/* 85 */     this.fClockPrescaler.setText(Integer.toString(traceDiv));
/* 86 */     this.fSwoClock.setText(String.format("%.1f", new Object[] { Double.valueOf(swoClock / 1000.0D) }));
/*    */   }
/*    */ 
/*    */   public boolean save(ILaunchConfigurationWorkingCopy config)
/*    */   {
/* 95 */     return true;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.config.ClockSettingsWidget
 * JD-Core Version:    0.6.2
 */