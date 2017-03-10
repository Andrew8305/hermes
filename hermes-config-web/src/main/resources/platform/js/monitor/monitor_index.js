var vue;

$(function(){
	vue = new Vue({
		el: '#app',
		data: function(){
			return {
				noMonitorVisible:false,
				monitorVisible:false,
				monitorDetailVisible:false,
				jobVisible:true,
				loading:{
					monitorLoadingVisible:false,
					jobLoadingVisible:false,
					cpuLoadingVisible:false,
					jvmLoadingVisible:false,
					memoryLoadingVisible:false,
					osLoadingVisible:false
				},
				monitors:[],
				monitor:{
					id:null,
					ip:null,
					jobNum:null,
					taskNum:null,
					stepNum:null
				},
				jobData:[],
				cpuStatus:[],
				jvmStatus:[],
				memoryStatus:[],
				osStatus:[]
			}
		},
		created:function(){
			this.loading.monitorLoadingVisible=true;
			this.initMonitor();
			setInterval(this.initMonitor,10000);
		},
		methods:{
			initMonitor:function(){
				PlatformUI.ajax({
					type:'GET',
					url:contextPath+'/monitor',
					data:{},
					afterOperation:function(data){
						if(data && !data.statusCode)vue.monitors = data;
						if(!vue.monitors || vue.monitors.length<=0){
							vue.noMonitorVisible=true;
							vue.monitorVisible=false;
						}else{
							vue.noMonitorVisible=false;
							vue.monitorVisible=true;
						}
						vue.loading.monitorLoadingVisible=false;
					}
				});
			},
			selectMonitor:function(monitor){
				vue.monitorDetailVisible=true;
				vue.loading.jobLoadingVisible=true;
				vue.loading.cpuLoadingVisible=true;
				vue.loading.jvmLoadingVisible=true;
				vue.loading.memoryLoadingVisible=true;
				vue.loading.osLoadingVisible=true;
				
				vue.monitor=monitor;
				var ip=vue.monitor.ip;
				var id=vue.monitor.id;
				var jobNum=vue.monitor.jobNum;
				var taskNum=vue.monitor.taskNum;
				var stepNum=vue.monitor.stepNum;
				PlatformUI.ajax({
					type:'GET',
					url:contextPath+'/monitor/getOne',
					data:{id:id},
					afterOperation:function(data){
						if(data.responseText==""){
							PlatformUI.message({message:"挂载点已失效", type:"warning"});
							vue.initMonitor();
							vue.monitorDetailVisible=false;
							vue.loading.jobLoadingVisible=false;
							vue.loading.cpuLoadingVisible=false;
							vue.loading.jvmLoadingVisible=false;
							vue.loading.memoryLoadingVisible=false;
							vue.loading.osLoadingVisible=false;
							return false;
						}
						//改变表单详情列表
						var jobs=data.etlReport.jobReports;
						var jobData = [];
						if(jobs){
							for(var i=0;i<jobs.length;i++){
								var jd = {};
								jd.jobId=jobs[i].jobId;
								var tasks=jobs[i].taskReports;
								if(tasks){
									jd.tasks=tasks.length;
									var job_stepNum=0;
									for(var j=0;j<tasks.length;j++){
										var job_stepNum_=tasks[j].stepNum;
										if(job_stepNum_){
											job_stepNum+=parseInt(job_stepNum_);
										}
									}
									jd.steps=job_stepNum;
								}else{
									jd.tasks=null;
									jd.steps=null;
								}
								jobData.push(jd);
							}
						}
						
						vue.jobData=jobData;
						vue.loading.jobLoadingVisible=false;
						//改变表单OS值
						vue.jvmStatus = data.machineReport.jvmStatus;
						vue.loading.jvmLoadingVisible=false;
						vue.memoryStatus = data.machineReport.memoryStatus;
						vue.loading.memoryLoadingVisible=false;
						vue.osStatus = data.machineReport.osStatus;
						vue.loading.osLoadingVisible=false;
						var cpuStatus = [];
						if(data.machineReport.cpuStatus){
							for(var i=0;i<data.machineReport.cpuStatus.length;i++){
								var cpuStatusInfo=data.machineReport.cpuStatus[i].cpuInfo;
								if(cpuStatusInfo){
									for(var j=0;j<cpuStatusInfo.length;j++){
										var cpu = {};
										if(j==0){
											cpu.ishead=true;
										}else{
											cpu.ishead=false;
										}
										cpu.text=cpuStatusInfo[j];
										cpuStatus.push(cpu);
									}
								}
							}
						}
						vue.cpuStatus = cpuStatus;
						vue.loading.cpuLoadingVisible=false;
					}
				});
			},
			showJobTable:function(){
				if(!vue.jobVisible){
					vue.jobVisible=true;
				}else{
					vue.jobVisible=false;
				}
			}
			
		}
	});
});

