package org.apel.hermes.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apel.hermes.monitor.api.report.CpuStatus;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

public class SigarUtil {

	public static List<String> jvm(){
		List<String> jvmInfo = new ArrayList<>();
		Runtime r = Runtime.getRuntime();
        Properties props = System.getProperties();
        InetAddress addr = null;
        try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
        String ip = addr.getHostAddress();
        Map<String, String> map = System.getenv();
        String userName = map.get("USERNAME");// 获取用户名
        String computerName = map.get("COMPUTERNAME");// 获取计算机名
        String userDomain = map.get("USERDOMAIN");// 获取计算机域名
        jvmInfo.add("用户名:    " + userName);
        jvmInfo.add("计算机名:    " + computerName);
        jvmInfo.add("计算机域名:    " + userDomain);
        jvmInfo.add("本地ip地址:    " + ip);
        jvmInfo.add("本地主机名:    " + addr.getHostName());
        jvmInfo.add("JVM可以使用的总内存:    " + r.totalMemory());
        jvmInfo.add("JVM可以使用的剩余内存:    " + r.freeMemory());
        jvmInfo.add("JVM可以使用的处理器个数:    " + r.availableProcessors());
        jvmInfo.add("Java的运行环境版本：    " + props.getProperty("java.version"));
        jvmInfo.add("Java的运行环境供应商：    " + props.getProperty("java.vendor"));
        jvmInfo.add("Java供应商的URL：    " + props.getProperty("java.vendor.url"));
        jvmInfo.add("Java的安装路径：    " + props.getProperty("java.home"));
        jvmInfo.add("Java的虚拟机规范版本：    " + props.getProperty("java.vm.specification.version"));
        jvmInfo.add("Java的虚拟机规范供应商：    " + props.getProperty("java.vm.specification.vendor"));
        jvmInfo.add("Java的虚拟机规范名称：    " + props.getProperty("java.vm.specification.name"));
        jvmInfo.add("Java的虚拟机实现版本：    " + props.getProperty("java.vm.version"));
        jvmInfo.add("Java的虚拟机实现供应商：    " + props.getProperty("java.vm.vendor"));
        jvmInfo.add("Java的虚拟机实现名称：    " + props.getProperty("java.vm.name"));
        jvmInfo.add("Java运行时环境规范版本：    " + props.getProperty("java.specification.version"));
        jvmInfo.add("Java运行时环境规范供应商：    " + props.getProperty("java.specification.vender"));
        jvmInfo.add("Java运行时环境规范名称：    " + props.getProperty("java.specification.name"));
        jvmInfo.add("操作系统的名称：    " + props.getProperty("os.name"));
        jvmInfo.add("操作系统的构架：    " + props.getProperty("os.arch"));
        jvmInfo.add("操作系统的版本：    " + props.getProperty("os.version"));
        jvmInfo.add("用户的账户名称：    " + props.getProperty("user.name"));
        jvmInfo.add("用户的主目录：    " + props.getProperty("user.home"));
        jvmInfo.add("用户的当前工作目录：    " + props.getProperty("user.dir"));
        return jvmInfo;
	}
	
	public static List<String> os(){
		List<String> osInfo = new ArrayList<>();
		OperatingSystem OS = OperatingSystem.getInstance();
        // 操作系统内核类型如： 386、486、586等x86
        osInfo.add("操作系统:    " + OS.getArch());
        osInfo.add("操作系统CpuEndian():    " + OS.getCpuEndian());//
        osInfo.add("操作系统DataModel():    " + OS.getDataModel());//
        // 系统描述
        osInfo.add("操作系统的描述:    " + OS.getDescription());
        // 操作系统类型
        // osInfo.add("OS.getName():    " + OS.getName());
        // osInfo.add("OS.getPatchLevel():    " + OS.getPatchLevel());//
        // 操作系统的卖主
        osInfo.add("操作系统的卖主:    " + OS.getVendor());
        // 卖主名称
        osInfo.add("操作系统的卖主名:    " + OS.getVendorCodeName());
        // 操作系统名称
        osInfo.add("操作系统名称:    " + OS.getVendorName());
        // 操作系统卖主类型
        osInfo.add("操作系统卖主类型:    " + OS.getVendorVersion());
        // 操作系统的版本号
        osInfo.add("操作系统的版本号:    " + OS.getVersion());
        return osInfo;
	}
	
	public static List<CpuStatus> cpu(){
		List<CpuStatus> cpuInfo = new ArrayList<>();
		CpuInfo infos[] = null;
		CpuPerc cpuList[] = null;
		try {
			Sigar sigar = new Sigar();
			infos = sigar.getCpuInfoList();
			cpuList = null;
			cpuList = sigar.getCpuPercList();
		} catch (SigarException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
        for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
            CpuInfo info = infos[i];
            CpuStatus cpuStatus = new CpuStatus();
            List<String> cpuStatusList = new ArrayList<>();
            cpuStatusList.add("第" + (i + 1) + "块CPU信息");
            cpuStatusList.add("CPU的总量MHz:    " + info.getMhz());// CPU的总量MHz
            cpuStatusList.add("CPU生产商:    " + info.getVendor());// 获得CPU的卖主，如：Intel
            cpuStatusList.add("CPU类别:    " + info.getModel());// 获得CPU的类别，如：Celeron
            cpuStatusList.add("CPU缓存数量:    " + info.getCacheSize());// 缓冲存储器数量
            cpuStatusList.add("CPU用户使用率:    " + CpuPerc.format(cpuList[i].getUser()));// 用户使用率
            cpuStatusList.add("CPU系统使用率:    " + CpuPerc.format(cpuList[i].getSys()));// 系统使用率
            cpuStatusList.add("CPU当前等待率:    " + CpuPerc.format(cpuList[i].getWait()));// 当前等待率
            cpuStatusList.add("CPU当前错误率:    " + CpuPerc.format(cpuList[i].getNice()));//
	        cpuStatusList.add("CPU当前空闲率:    " + CpuPerc.format(cpuList[i].getIdle()));// 当前空闲率
	        cpuStatusList.add("CPU总的使用率:    " + CpuPerc.format(cpuList[i].getCombined()));// 总的使用率
	        cpuStatus.setCpuInfo(cpuStatusList);
	        cpuInfo.add(cpuStatus);
        }
        return cpuInfo;
	}
	
	public static List<String> memory(){
		List<String> memeryInfo = new ArrayList<>();
		Sigar sigar = new Sigar();
        Mem mem;
		Swap swap;
		try {
			mem = sigar.getMem();
			swap = sigar.getSwap();
		} catch (SigarException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
        // 内存总量
        memeryInfo.add("内存总量:    " + mem.getTotal() / 1024L + "K av");
        // 当前内存使用量
        memeryInfo.add("当前内存使用量:    " + mem.getUsed() / 1024L + "K used");
        // 当前内存剩余量
        memeryInfo.add("当前内存剩余量:    " + mem.getFree() / 1024L + "K free");
        
        // 交换区总量
        memeryInfo.add("交换区总量:    " + swap.getTotal() / 1024L + "K av");
        // 当前交换区使用量
        memeryInfo.add("当前交换区使用量:    " + swap.getUsed() / 1024L + "K used");
        // 当前交换区剩余量
        memeryInfo.add("当前交换区剩余量:    " + swap.getFree() / 1024L + "K free");
        return memeryInfo;
	}

	public static String ip() {
		InetAddress addr = null;
        try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
        String ip = addr.getHostAddress();
		return ip;
	}
	
}
