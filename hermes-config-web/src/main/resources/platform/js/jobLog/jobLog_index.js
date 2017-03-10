var vue;
var job_grid;
var task_grid;
var step_grid;


$(function(){
	
	vue = new Vue({
		el: '#app',
		data: function(){
			return {
				checkboxCols:[],//列表字段
				dialogGridSelectionVisible: false,//列表字段选择dialog显示
				fullscreenLoading: false,//全屏loading条
				toolBarForm: {
					value: "",
					condition: ""
				},
		        formRules: {
					jobBizId:[
					],
					jobName:[
					]
		        },
		        dialogFormVisible: false,//dialog是否显示
		        formEdit: false,
		        submitBtnName: "立即创建",
		        logDetailVisible:false,
		        stepDetailVisible:false,
		        taskForm:{
		        	jobBizId:null,
		        	startTime:null,
		        	endTime:null,
		        	duration:null,
		        	jobName:null
		        },
		        stepDetail:[
		        ],
		        taskListVisible: false,
		        stepListVisible:false
			};
		},
		methods:{
			compositeSearch: function(){//检索
				var context = this;
				job_grid.jqGrid("setGridParam", {
					postData: {filters:{}}
				});
				job_grid.jqGrid('searchGrid', {multipleSearch:true,drag:false,searchOnEnter:true,
					onSearch: function(){
						FieldtypeAddtionerFactory.create(job_grid).search();
						context.toolBarForm.value = '';
						context.toolBarForm.condition = '';
					}
				});
			},
			refreshPage: function(){//刷新
				this.fullscreenLoading = true;
				setTimeout(function(){
					location.reload();
				}, 1000);
			},
			exp: function(){//导出
				PlatformUI.exportGrid("list", "from etl_job_log");
			},
			resetForm: function(){
				this.dialogFormVisible = false;
				this.$refs['form'].resetFields();
			},
		    commonSearch: function(value){
		    	commonSearch();
		    },
		    selectGridColumn: function(){
		    	this.dialogGridSelectionVisible = true;
		    },
		    saveColVisible: function(){
		    	for(var i = 0; i < this.checkboxCols.length; i++){
		    		if(this.checkboxCols[i].visible){
		    			job_grid.showCol(this.checkboxCols[i].value);
		    		}else{
		    			job_grid.hideCol(this.checkboxCols[i].value);
		    		}
		    	}
		    	this.dialogGridSelectionVisible = false;
		    	//重设jqrid宽度
		    	PlatformUI.fineTuneGridSize(job_grid, 35);
		    },
		    showLogDetail:function(){
		    	//填充数据
		    	var ids = job_grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length != 1){
					PlatformUI.message({message:"请选择一条要查看的数据!", type:"warning"});
					return;
				}
				//添加作业数据
				var job=job_grid.jqGrid ('getRowData', ids[0]);
		    	this.logDetailVisible = true;
		    	this.taskListVisible = true;
		    	this.stepListVisible = true;
		    	this.taskForm.jobBizId=job.jobBizId;
		    	this.taskForm.jobName=job.jobName; 
		    	this.taskForm.startTime=job.startTime;
		    	this.taskForm.endTime=job.endTime;
		    	this.taskForm.duration=job.duration;
		    	//加载步骤表
		    	loadLogDetailLists();
		    }
		}
	});
	
	//绑定jqgrid resize事件
	$(window).bind('resize', function() {
		PlatformUI.resizeGridWidth(job_grid, 35);
	});
	
	job_grid = $("#job_grid").jqGrid({
        url: contextPath + "/jobLog",
        datatype: "json",
        autowidth: true,
        height:300,
        mtype: "GET",
        multiselect: true,
        colNames: ['ID','作业标识','作业名称','开始时间','结束时间','持续时间','创建时间'],
        colModel: [
			{ name: 'id', index:'id',hidden: true},
			{ name: 'jobBizId', index:'jobBizId', align:'center', sortable: true},
			{ name: 'jobName', index:'jobName', align:'center', sortable: true},
			{ name: 'createDate', index:'createDate',align:'center', expType:'date',expValue:'yyyy-MM-dd',searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:'date',formatoptions: { srcformat: 'U', newformat: 'Y-m-d H:i:s' }},
			{ name: 'endTime', index:'endTime',align:'center', expType:'date',expValue:'yyyy-MM-dd',searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:'date',formatoptions: { srcformat: 'U', newformat: 'Y-m-d H:i:s' }},
			{ name: 'duration', index:'duration', align:'center', sortable: true},
			{ name: 'startTime', index:'startTime',align:'center', expType:'date',expValue:'yyyy-MM-dd',searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:'date',formatoptions: { srcformat: 'U', newformat: 'Y-m-d H:i:s' }}
        ],
        pager: "#job_pager",
        rowNum: 10,
        rowList: [10, 20, 30],
        sortname: "createDate",
        sortorder: "desc",
        viewrecords: true,
        gridview: true,
        autoencode: true,
        caption: "作业日志列表",
    	gridComplete: function(){
    		PlatformUI.fineTuneGridSize(job_grid, 42);
    		//设置隐藏/显示列字段
    		vue.checkboxCols = [];
    		var gridColModel = job_grid.getGridParam("colModel");
	    	var gridColNames = job_grid.getGridParam("colNames");
	    	for(var i=0; i < gridColNames.length; i++){
	    		if(gridColNames[i].indexOf("role='checkbox'") == -1){
		    		var name = gridColNames[i];
					var value = gridColModel[i].name;
					var visible = true;
					if(gridColModel[i].hidden){
						visible = false;
					}
		    		vue.checkboxCols.push({name:name, value:value, visible:visible});
	    		}
	    	}
    	}
    });
	
	
			
});

/***********************方法区***************************/

function commonSearch(){
	var name = vue.toolBarForm.condition;
	var value = vue.toolBarForm.value;
	if(name == ""){
		PlatformUI.message({message:"请选择搜索条件", type:"warning"});
		return;
	}
	if(value == ""){
		PlatformUI.message({message:"请输入搜索内容", type:"warning"});
		return;
	}
	var rules = [{"field":name,"op":"cn","data":value.trim()}];
	var filters = {"groupOp":"AND","rules":rules};
	job_grid.jqGrid("setGridParam", {
		postData: {filters:JSON.stringify(filters)},
		page: 1
	}).trigger("reloadGrid");
}

function createTaskGrid(){
	task_grid = $("#task_list").jqGrid({
		url: contextPath + "/jobLog/taskLog",
		datatype: "json",
		autowidth: true,
		height:200,
		mtype: "GET",
		multiselect: false,
		colNames: ['ID','任务标识','开始时间','结束时间','持续时间','是否成功','结果描述'],
		colModel: [
		           { name: 'id', index:'id',hidden: true},
		           { name: 'taskBizId', index:'taskBizId', align:'center', sortable: true},
		           { name: 'startTime', index:'startTime',align:'center', expType:'date',expValue:'yyyy-MM-dd',searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:'date',formatoptions: { srcformat: 'U', newformat: 'Y-m-d H:i:s' }},
		           { name: 'endTime', index:'endTime',align:'center', expType:'date',expValue:'yyyy-MM-dd',searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:'date',formatoptions: { srcformat: 'U', newformat: 'Y-m-d H:i:s' }},
		           { name: 'duration', index:'duration', align:'center', sortable: true},
		           { name:'result', index:'result',expType:'boolean',expValue:{'true':'是','false':'否'},align:'center', formatter: PlatformUI.defaultYNFormatter, stype:'select', searchoptions:{value:'true:是;false:否'}},
		           { name: 'resultDesc', index:'resultDesc', align:'center', sortable: true}
		           ],
		           pager: "#task_pager",
		           rowNum: 10,
		           rowList: [10, 20, 30],
		           sortname: "createDate",
		           sortorder: "desc",
		           viewrecords: true,
		           gridview: true,
		           autoencode: true,
		           caption: "任务日志列表",
		           onSelectRow:function(rowid,status,e){
		        	   var rules = [];
		        	   if(rowid){
		        		   rules.push({"field":"taskLogId","op":"eq","data":rowid});
		        	   }else{
		        		   return;
		        	   }
		        	   var filters = {"groupOp":"AND","rules":rules};
		        	   step_grid.jqGrid("setGridParam", {
		        		   postData: {filters:JSON.stringify(filters)},
		        		   datatype:'json',
		        		   page: 1,
		        		   ondblClickRow:function(rowid,status,e){
				        	   var logs=step_grid.getRowData(rowid).logDetails;
				        	   if(logs){
				        		   aa = logs.split("<br>");
				        	   }else{
				        		   if(!aa)aa=["没有其他信息"];
				        	   }
				        	   vue.stepDetail=aa;
				        	   vue.stepDetailVisible=true;
				           }
		        	   }).trigger("reloadGrid");
		           },
		           gridComplete: function(){
		        	   vue.taskListVisible = false;
		           }
	});
}

function createStepGrid(){
	step_grid = $("#step_list").jqGrid({
		url: contextPath + "/jobLog/taskLog/stepLog",
		datatype: "local",
		autowidth: true,
		height:200,
		mtype: "GET",
		multiselect: false,
		colNames: ['ID','顺序','开始时间','结束时间','持续时间','是否成功','结果描述','步骤日志'],
		colModel: [
		           { name: 'id', index:'id',hidden: true},
		           { name: 'seq', index:'seq', align:'center', sortable: true},
		           { name: 'startTime', index:'startTime',align:'center', expType:'date',expValue:'yyyy-MM-dd',searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:'date',formatoptions: { srcformat: 'U', newformat: 'Y-m-d H:i:s' }},
		           { name: 'endTime', index:'endTime',align:'center', expType:'date',expValue:'yyyy-MM-dd',searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:'date',formatoptions: { srcformat: 'U', newformat: 'Y-m-d H:i:s' }},
		           { name: 'duration', index:'duration', align:'center', sortable: true},
		           { name:'result', index:'result',expType:'boolean',expValue:{'true':'是','false':'否'},align:'center', formatter: PlatformUI.defaultYNFormatter, stype:'select', searchoptions:{value:'true:是;false:否'}},
		           { name: 'resultDesc', index:'resultDesc', align:'center', sortable: true},
		           { name: "logDetails", index:"logDetails", align:"center", hidden: true},
		           ],
		           pager: "#step_pager",
		           rowNum: 10,
		           rowList: [10, 20, 30],
		           sortname: "createDate",
		           sortorder: "desc",
		           viewrecords: true,
		           gridview: true,
		           autoencode: true,
		           caption: "过程日志列表" ,
		           gridComplete: function(){
		        	   vue.stepListVisible = false;
		           }   
	});
}

function loadLogDetailLists(){
	setTimeout(function(){
		if(task_grid){
			PlatformUI.refreshGrid(task_grid, {sortname:"createDate",sortorder:"desc"});
		}else{
			createTaskGrid();
		}
		if(step_grid){
			var rules = [];
			var rowid = null;
     	   	if(rowid){
     		   rules.push({"field":"taskLogId","op":"eq","data":rowid});
     		   var filters = {"groupOp":"AND","rules":rules};
     		   step_grid.jqGrid("setGridParam", {
	       		   postData: {filters:JSON.stringify(filters)},
	       		   datatype:'json',
	       		   page: 1
	       	   }).trigger("reloadGrid");
     	    }
     	   	vue.stepListVisible = false;
		}else{
			createStepGrid();
		}
	}, 400);
}

