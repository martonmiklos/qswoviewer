/*     */ package com.atollic.truestudio.swv.core;
/*     */ 
/*     */ import com.atollic.truestudio.common.toolchain.export.CpuCoreEnum;
/*     */ import com.atollic.truestudio.productmanager.exposed.TSProjectManager;
/*     */ import com.atollic.truestudio.tsp.export.IMcu;
/*     */ import com.atollic.truestudio.tsp.export.TargetSupportPackage;
/*     */ import java.util.Optional;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.debug.core.ILaunchConfiguration;
/*     */ 
/*     */ public class ProjectInfo
/*     */ {
/*     */   private String projectName;
/*     */   private String vendor;
/*     */   private String mcu;
/*     */   private CpuCoreEnum cpuCore;
/*     */ 
/*     */   public ProjectInfo(String projectName, String vendor, String mcu, CpuCoreEnum cpuCore)
/*     */   {
/*  19 */     this.projectName = projectName;
/*  20 */     this.vendor = vendor;
/*  21 */     this.mcu = mcu;
/*  22 */     this.cpuCore = cpuCore;
/*     */   }
/*     */ 
/*     */   public ProjectInfo(ILaunchConfiguration activeConfig) {
/*  26 */     if (activeConfig != null) {
/*     */       try {
/*  28 */         setProjectName(activeConfig.getAttribute("org.eclipse.cdt.launch.PROJECT_ATTR", ""));
/*     */       } catch (CoreException e) {
/*  30 */         setProjectName("");
/*     */       }
/*     */ 
/*  34 */       setVendor(TSProjectManager.getProjectSetting(getProjectName(), "MCU_VENDOR", ""));
/*  35 */       setMcu(TSProjectManager.getProjectSetting(getProjectName(), "MCU", ""));
/*     */ 
/*  37 */       IMcu mcu = (IMcu)TargetSupportPackage.getTargetSupportPackage(getVendor()).findMcu(getMcu()).orElse(null);
/*  38 */       if (mcu != null)
/*  39 */         setCpuCore(mcu.getCpuCore());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getProjectName()
/*     */   {
/*  45 */     return this.projectName;
/*     */   }
/*     */ 
/*     */   private void setProjectName(String projectName) {
/*  49 */     this.projectName = projectName;
/*     */   }
/*     */ 
/*     */   public String getVendor() {
/*  53 */     return this.vendor;
/*     */   }
/*     */ 
/*     */   private void setVendor(String vendor) {
/*  57 */     this.vendor = vendor;
/*     */   }
/*     */ 
/*     */   public String getMcu() {
/*  61 */     return this.mcu;
/*     */   }
/*     */ 
/*     */   private void setMcu(String mcu) {
/*  65 */     this.mcu = mcu;
/*     */   }
/*     */ 
/*     */   public CpuCoreEnum getCpuCore() {
/*  69 */     return this.cpuCore;
/*     */   }
/*     */ 
/*     */   private void setCpuCore(CpuCoreEnum cpuCore)
/*     */   {
/*  76 */     this.cpuCore = cpuCore;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  85 */     int prime = 31;
/*  86 */     int result = 1;
/*  87 */     result = 31 * result + (this.mcu == null ? 0 : this.mcu.hashCode());
/*  88 */     result = 31 * result + (this.vendor == null ? 0 : this.vendor.hashCode());
/*  89 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  97 */     if (this == obj) {
/*  98 */       return true;
/*     */     }
/* 100 */     if (obj == null) {
/* 101 */       return false;
/*     */     }
/* 103 */     if (!(obj instanceof ProjectInfo)) {
/* 104 */       return false;
/*     */     }
/* 106 */     ProjectInfo other = (ProjectInfo)obj;
/* 107 */     if (this.mcu == null) {
/* 108 */       if (other.mcu != null)
/* 109 */         return false;
/*     */     }
/* 111 */     else if (!this.mcu.equals(other.mcu)) {
/* 112 */       return false;
/*     */     }
/* 114 */     if (this.vendor == null) {
/* 115 */       if (other.vendor != null)
/* 116 */         return false;
/*     */     }
/* 118 */     else if (!this.vendor.equals(other.vendor)) {
/* 119 */       return false;
/*     */     }
/* 121 */     return true;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 126 */     return getProjectName() + " : vendor: " + getVendor() + " : mcu: " + getMcu() + " - " + getCpuCore();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/* 130 */     return (getVendor().isEmpty()) && (getMcu().isEmpty());
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ProjectInfo
 * JD-Core Version:    0.6.2
 */