var grid;
var vue;
var taskGrid;

$(function(){
	
	vue = new Vue({
		el: '#app',
		data: function(){
			return {
				checkboxCols:[],//列表字段
				taskCheckboxCols:[],//task
				dialogGridSelectionVisible: false,//列表字段选择dialog显示
				taskDialogGridSelectionVisible:false,
				fullscreenLoading: false,//全屏loading条
				toolBarForm: {
					value: "",
					condition: ""
				},//top工具form数据,
				taskToolBarForm:{
					value: "",
					condition: ""
				},
				jobId:'',
				form: {_out:null,jobName:null,description:null,_in:null,jobKey:null,schedule:null},
				jobDBConfigureIn:[],
				jobDBConfigureOut:[],
				taskDBConfigureIn:[],
				taskDBConfigureOut:[],
				formRules: {
					jobName:[
						{ required: 'true', message: '必填项', trigger: 'blur' }
					],
					description:[
					],
					jobKey:[
						{ required: 'true', message: '必填项', trigger: 'blur' }
					],
					schedule:[
						{ required: 'true', message: '必填项', trigger: 'blur' }
					]

		        },
		        
		        taskForm:{taskKey:null,dbInput:null,dbOutput:null},
		        taskFormRules: {
		        	taskKey:[
						{ required: 'true', message: '必填项', trigger: 'blur' }
					],
					dbInput:[
						{ required: 'true', message: '必填项', trigger: 'blur' }
					],
					dbOutput:[
						{ required: 'true', message: '必填项', trigger: 'blur' }
					]
		        },
		        dialogFormVisible: false,//dialog是否显示
		        formEdit: false,
		        submitBtnName: "立即创建",
		        dBdata:[],
		        taskDialogFormVisible:false
			};
		},
		methods:{
			compositeSearch: function(){//检索
				var context = this;
				grid.jqGrid("setGridParam", {
					postData: {filters:{}}
				});
				grid.jqGrid('searchGrid', {multipleSearch:true,drag:false,searchOnEnter:true,
					onSearch: function(){
						FieldtypeAddtionerFactory.create(grid).search();
						context.toolBarForm.value = '';
						context.toolBarForm.condition = '';
					}
				});
			},
			taskCompositeSearch: function(){//检索
				var context = this;
				taskGrid.jqGrid("setGridParam", {
					postData: {filters:{}}
				});
				taskGrid.jqGrid('searchGrid', {multipleSearch:true,drag:false,searchOnEnter:true,
					onSearch: function(){
						FieldtypeAddtionerFactory.create(grid).search();
						context.taskToolBarForm.value = '';
						context.taskToolBarForm.condition = '';
					}
				});
			},
			addDb:function(oper){
				var flag = false;
				if(oper=="in" && null != vue.form._in ){
					var data = vue.form._in;
					$.each(this.jobDBConfigureIn,function(index,obj){
						if(obj.id==data.id){
							flag =true;
							return;
						}
					});
					if(!flag){
						this.jobDBConfigureIn.push(data);
					}
				}
				if(oper=="out" && null != vue.form._out ){
					var data = vue.form._out;
					$.each(this.jobDBConfigureOut,function(index,obj){
						if(obj.id==data.id){
							flag =true;
							return;
						}
					});
					if(!flag){
						this.jobDBConfigureOut.push(data);
					}
				}
			},
			dbDel:function($index,oper){
				var jobDbId = "";
				if(oper=="in" && undefined!=this.jobDBConfigureIn[$index] && undefined!=this.jobDBConfigureIn[$index].jobdbId &&
						this.jobDBConfigureIn[$index].jobdbId && this.jobDBConfigureIn[$index].jobdbId!=""){
					jobDbId = this.jobDBConfigureIn[$index].jobdbId;
				}
				if(oper=="out" && undefined!=this.jobDBConfigureOut[$index] && undefined!=this.jobDBConfigureOut[$index].jobdbId &&
						this.jobDBConfigureOut[$index].jobdbId && this.jobDBConfigureOut[$index].jobdbId!=""){
					jobDbId = this.jobDBConfigureOut[$index].jobdbId;
				}
				if(oper=="in"){
					this.jobDBConfigureIn.splice($index,1);
				}else{
					this.jobDBConfigureOut.splice($index,1);
				}
				if(jobDbId!=""){
		        	PlatformUI.ajax({
						url: contextPath + "/jobDBConfigure/"+jobDbId,
						type: "post",
						data: {_method:"delete"},
						message:PlatformUI.message,
						afterOperation: function(){
							PlatformUI.refreshGrid(grid, {sortname:"createDate",sortorder:"desc"});
						}
					});
				       
				}
				loadDBConfig();
			},
			refreshPage: function(){//刷新
				this.fullscreenLoading = true;
				setTimeout(function(){
					location.reload();
				}, 1000);
			},
			add: function(){//新增
				this.dialogFormVisible = true;
				this.formEdit = false;
				this.submitBtnName = "立即创建";
				loadDBConfig();
			},
			edit: function(){//编辑
				var ids = grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length != 1){
					PlatformUI.message({message: "选择一条要编辑的数据!", type: "warning"});
					return;
				}
				var context = this;
				this.dialogFormVisible = true;
				this.formEdit = true;
				this.submitBtnName = "编辑提交";
				PlatformUI.ajax({
					url: contextPath + "/job/" + ids[0],
					afterOperation: function(data, textStatus,jqXHR){
						delete data.createDate;
						context.form = $.extend(context.form, data);
						editloadDBConfig();
						loadDBConfig();
						
						
					}
				});
			},
			taskAddDb:function(oper){
				var flag = false;
				if(oper=="in" && null != vue.taskForm.dbInput ){
					var data = vue.taskForm.dbInput;
					$.each(this.taskDBConfigureIn,function(index,obj){
						if(obj.id==data.id){
							flag =true;
							return;
						}
					});
					if(!flag){
						this.taskDBConfigureIn.push(data);
					}
				}
				if(oper=="out" && null != vue.taskForm.dbOutput ){
					var data = vue.taskForm.dbOutput;
					$.each(this.taskDBConfigureOut,function(index,obj){
						if(obj.id==data.id){
							flag =true;
							return;
						}
					});
					if(!flag){
						this.taskDBConfigureOut.push(data);
					}
				}
				
			},
			taskDbDel:function($index,oper){
				var taskDbId = "";
				if(oper=="in" && undefined!=this.taskDBConfigureIn[$index] && undefined!=this.taskDBConfigureIn[$index].taskDbId &&
						this.taskDBConfigureIn[$index].taskDbId && this.taskDBConfigureIn[$index].taskDbId!=""){
					taskDbId = this.taskDBConfigureIn[$index].taskDbId;
				}
				if(oper=="out" && undefined!=this.taskDBConfigureOut[$index] && undefined!=this.taskDBConfigureOut[$index].taskDbId &&
						this.taskDBConfigureOut[$index].taskDbId && this.taskDBConfigureOut[$index].taskDbId!=""){
					taskDbId = this.taskDBConfigureOut[$index].taskDbId;
				}
				
				if(oper=="in"){
					
					
					this.taskDBConfigureIn.splice($index,1);
				}else{
					this.taskDBConfigureOut.splice($index,1);
				}
				if(taskDbId!=""){
		        	PlatformUI.ajax({
						url: contextPath + "/taskDBConfigure/"+taskDbId,
						type: "post",
						data: {_method:"delete"},
						message:PlatformUI.message,
						afterOperation: function(){
							
							//PlatformUI.refreshGrid(grid, {sortname:"createDate",sortorder:"desc"});
						}
					});
				       
				}
			},
			taskEdit:function(){
				var ids = taskGrid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length != 1){
					PlatformUI.message({message: "选择一条要编辑的数据!", type: "warning"});
					return;
				}
				var context = this;
				this.formEdit = true;
				this.submitBtnName = "编辑提交";
				PlatformUI.ajax({
					url: contextPath + "/taskDBConfigure/" + ids[0],
					afterOperation: function(data, textStatus,jqXHR){
						context.taskForm = $.extend(context.taskForm, data[0].task);
						vue.taskDBConfigureIn=[];
						vue.taskDBConfigureOut=[];
						$.each(data,function(index,obj){
							if(obj.inDBconfigure!=null){
								var outData = obj.inDBconfigure;
								vue.taskDBConfigureIn.push({id:outData.id,dbName:outData.dbName,taskDbId:obj.id});
							}
							if(obj.outDBconfigure!=null){
								var outData = obj.outDBconfigure;
								vue.taskDBConfigureOut.push({id:outData.id,dbName:outData.dbName,taskDbId:obj.id});
							}
						});
					}
				});
				
				
			},
			del: function(){//删除 
				var ids = grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length == 0){
					PlatformUI.message({message:"请至少选择一条要删除的数据!", type:"warning"});
					return;
				}
				this.$confirm('此操作将永久删除JOb相关任务数据, 是否继续?', '提示', {
		          confirmButtonText: '确定',
		          cancelButtonText: '取消',
		          type: 'warning'
		        }).then(function(){
		        	PlatformUI.ajax({
						url: contextPath + "/job",
						type: "post",
						data: {_method:"delete",ids:ids},
						message:PlatformUI.message,
						afterOperation: function(){
							PlatformUI.refreshGrid(grid, {sortname:"createDate",sortorder:"desc"
							});
						}
					});
		        });
			},
			taskDel:function(){
				var ids = taskGrid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length == 0){
					PlatformUI.message({message:"请至少选择一条要删除的数据!", type:"warning"});
					return;
				}
				
				this.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
		          confirmButtonText: '确定',
		          cancelButtonText: '取消',
		          type: 'warning'
		        }).then(function(){
		        	PlatformUI.ajax({
						url: contextPath + "/task",
						type: "post",
						data: {_method:"delete",ids:ids},
						message:PlatformUI.message,
						afterOperation: function(){
							PlatformUI.refreshGrid(taskGrid, {sortname:"createDate",sortorder:"desc",
								postData: {filters:defaultCondition()}		
							});
						}
					});
		        });
			},
			resetForm: function(){
				this.dialogFormVisible = false;
				vue.formEdit = false;
				this.$refs['form'].resetFields();
				
				this.jobDBConfigureIn=[];
				this.jobDBConfigureOut=[];
				this.form = {_out:null,jobName:null,description:null,_in:null,jobKey:null,schedule:null};
			},
			onSubmit: function(){//弹出表单的提交
				var context = this;
        		this.$refs['form'].validate(function(valid){
        			if (valid) {
        				var data = $.extend({}, context.form);
        				//验证是否有数据源
        				if(context.jobDBConfigureOut.length==0 || context.jobDBConfigureIn.length==0){
        					  PlatformUI.message({message:"必须填写数据源信息", type:"error"});
        					return;
        				}
        				var inList = [];
        				$.each(context.jobDBConfigureIn,function(index,obj){
        					inList.push(obj.id);
        				});
        				var outList=[];
        				$.each(context.jobDBConfigureOut,function(index,obj){
        					outList.push(obj.id);
        				});
        				data.inList = inList;
        				data.outList = outList;
        				var actionUrl = contextPath + "/job";
        				if(context.formEdit){
        					actionUrl = contextPath + "/job/update";
				            data.id = data.id;
        				}
        				PlatformUI.ajax({
        		 			headers: {
        		 		        'Accept': 'application/json', 
        		 		        'Content-Type': 'application/json' 
        		 		    },
        		 		    url: actionUrl,
        		 			type: "post",
        		 			data: JSON.stringify(data),
        					afterOperation: function(data){
        						context.toolBarForm.value = "";
			            		context.toolBarForm.condition = "";
			            		if(context.formEdit){
			            			vue.formEdit = false;
			            		}
			            		
			            		PlatformUI.refreshGrid(grid, {sortname:"createDate",sortorder:"desc"		
			            		});
        					}
        		 		});
        				
        				
        				
			            context.dialogFormVisible = false;
						context.$refs['form'].resetFields();
			        } else {
			            PlatformUI.message({message:"表单验证失败", type:"error"});
			            return false;
			        }
        		});
     		},
		    commonSearch: function(value){
		    	commonSearch();
		    },
		    taskCommonSearch: function(value){
		    	taskCommonSearch();
		    },
		    selectGridColumn: function(){
		    	this.dialogGridSelectionVisible = true;
		    },
		    taskSelectGridColumn: function(){
		    	this.taskDialogGridSelectionVisible = true;
		    },
		    saveColVisible: function(){
		    	for(var i = 0; i < this.checkboxCols.length; i++){
		    		if(this.checkboxCols[i].visible){
		    			grid.showCol(this.checkboxCols[i].value);
		    		}else{
		    			grid.hideCol(this.checkboxCols[i].value);
		    		}
		    	}
		    	this.dialogGridSelectionVisible = false;
		    	//重设jqrid宽度
		    	PlatformUI.fineTuneGridSize(grid, 35);
		    },
		    taskSaveColVisible: function(){
		    	for(var i = 0; i < this.taskCheckboxCols.length; i++){
		    		if(this.taskCheckboxCols[i].visible){
		    			taskGrid.showCol(this.taskCheckboxCols[i].value);
		    		}else{
		    			taskGrid.hideCol(this.taskCheckboxCols[i].value);
		    		}
		    	}
		    	this.taskDialogGridSelectionVisible = false;
		    	//重设jqrid宽度
		    },
		    taskConfigure:function(){
		    	
		    	var ids = grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length != 1){
					PlatformUI.message({message: "选择一条要编辑的数据!", type: "warning"});
					return;
				}
				editloadDBConfig();
				var context = this;
				this.taskDialogFormVisible = true;
				vue.jobId = ids[0];
				
				setTimeout(function(){
					if(taskGrid){
						PlatformUI.refreshGrid(taskGrid, {
							sortname:"createDate",sortorder:"desc",
							postData:{filters:defaultCondition()}
						});
					}else{
						loadTask();
					}
				},400);
		    },
		    taskOnSubmit:function(){
		    	var context = this;
		    	
        		this.$refs['taskForm'].validate(function(valid){
        			if (valid) {
        				var data = $.extend({}, context.taskForm);
        				//验证是否有数据源
        				var actionUrl = contextPath + "/task";
        				if(context.formEdit){
        					actionUrl = contextPath + "/task/update";
				            data.id = data.id;
        				}else{
        					if(vue.taskDBConfigureIn==null || vue.taskDBConfigureOut==null
        						|| 	vue.taskDBConfigureIn.length==0  || 	vue.taskDBConfigureOut.length==0
        						){
        						PlatformUI.message({message: "输入源和输出源必选项!", type: "warning"});
        						return;
        					}
        					
        					
        				}
        				var job = {};
        				job.id = vue.jobId;
        				data.job = job;
        				data.inList = vue.taskDBConfigureIn;
        				data.outList=  vue.taskDBConfigureOut;
        				PlatformUI.ajax({
        		 			headers: {
        		 		        'Accept': 'application/json', 
        		 		        'Content-Type': 'application/json' 
        		 		    },
        		 		    url: actionUrl,
        		 		    type: "post",
        		 			data: JSON.stringify(data),
        					afterOperation: function(data){
        						context.taskToolBarForm.value = "";
			            		context.taskToolBarForm.condition = "";
			            		
			            		if(context.formEdit){
			            			this.taskDBConfigureIn=[];
			        				this.taskDBConfigureOut=[];
			            		}
			            		
			            		context.formEdit = false;
			            		PlatformUI.refreshGrid(taskGrid, {
			            			sortname:"createDate",sortorder:"desc",
			            			postData: {filters:defaultCondition()}	
			            		});
        					}
        		 		});
						//context.$refs['taskForm'].resetFields();
        				vue.taskResetForm();
        				var ids = grid.jqGrid ('getGridParam', 'selarrrow');
        				editloadDBConfig();
        			} else {
			            PlatformUI.message({message:"表单验证失败", type:"error"});
			            return false;
			        }
        		});
		    	
		    },
		    taskResetForm:function(){
		    	
		    	vue.formEdit = false;
		    	
		    	//this.taskDialogFormVisible = false;
				this.$refs['taskForm'].resetFields();
				this.taskDBConfigureIn=[];
				this.taskDBConfigureOut=[];
				
				this.taskForm = {taskKey:null,dbInput:null,dbOutput:null};
				
		    }
		}
	});
	
	//绑定jqgrid resize事件
	$(window).bind('resize', function() {
		PlatformUI.resizeGridWidth(grid, 35);
		
	});
	
	grid = $("#list").jqGrid({
        url: contextPath + "/job",
        datatype: "json",
        autowidth: true,
        height:300,
        mtype: "GET",
        multiselect: true,
        colNames: ['ID','作业名称','作业KEY','描述','cron表达式','创建时间'],
        colModel: [
			{ name: 'id', index:'id',hidden: true},
			{ name: 'jobName', index:'jobName', align:'center', sortable: true},
			{ name: 'jobKey', index:'jobKey', align:'center', sortable: true},
			{ name: 'description', index:'description', align:'center', sortable: true},
			{ name: 'schedule', index:'schedule', align:'center', sortable: true},
			{ name: 'createDate', index:'createDate',align:'center', expType:'date',expValue:'yyyy-MM-dd',searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:'date',formatoptions: { srcformat: 'U', newformat: 'Y-m-d H:i:s' }}
        ],
        pager: "#pager",
        rowNum: 10,
        rowList: [10, 20, 30],
        sortname: "createDate",
        sortorder: "desc",
        viewrecords: true,
        gridview: true,
        autoencode: true,
        caption: "列表",
    	gridComplete: function(){
    		PlatformUI.fineTuneGridSize(grid, 42);
    		//设置隐藏/显示列字段
    		vue.checkboxCols = [];
    		var gridColModel = grid.getGridParam("colModel");
	    	var gridColNames = grid.getGridParam("colNames");
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
	grid.jqGrid("setGridParam", {
		postData: {filters:JSON.stringify(filters)},
		page: 1
	}).trigger("reloadGrid");
}

function taskCommonSearch(){
	var name = vue.taskToolBarForm.condition;
	var value = vue.taskToolBarForm.value;
	
	var rules = [];
	if(name != "" && value != ""){
		rules.push({"field":name,"op":"cn","data":value.trim()});
	}
	
	rules.push({"field":"job.id","op":"cn","data":vue.jobId});
	var filters = {"groupOp":"AND","rules":rules};
	taskGrid.jqGrid("setGridParam", {
		postData: {filters:JSON.stringify(filters)},
		page: 1
	}).trigger("reloadGrid");
}

function loadDBConfig(){
	vue.dBdata = [];
	PlatformUI.ajax({
		url: contextPath + "/dBConfigure/all",
		afterOperation: function(data, textStatus,jqXHR){
			vue.dBdata=data;
		}
	});
}


function editloadDBConfig(){
	
	var ids = grid.jqGrid ('getGridParam', 'selarrrow');
	var id = ids[0];
	vue.jobDBConfigureIn = [];
	vue.jobDBConfigureOut=[];
	
	PlatformUI.ajax({
		url: contextPath + "/jobDBConfigure/"+id,
		afterOperation: function(data, textStatus,jqXHR){
			$.each(data,function(index,obj){
				if(obj.outDBconfigure!=null){
					var outData = obj.outDBconfigure;
					vue.jobDBConfigureOut.push({id:outData.id,dbName:outData.dbName,jobdbId:obj.id});
				}
				if(obj.inDBconfigure!=null){
					var inData = obj.inDBconfigure;
					vue.jobDBConfigureIn.push({id:inData.id,dbName:inData.dbName,jobdbId:obj.id});
				}
			});
		}
	});
}


function loadTask(){
	
	taskGrid = $("#taskList").jqGrid({
        url: contextPath + "/task",
        datatype: "json",
        autowidth: true,
        height:150,
        postData:{filters:defaultCondition()},
        mtype: "GET",
        multiselect: true,
        colNames: ['ID','job名称','任务KEY','创建时间','创建时间'],
        colModel: [
			{ name: 'id', index:'id',hidden: true},
			{ name: 'job.jobName', index:'job.jobName', align:'center', sortable: true},
			{ name: 'taskKey', index:'taskKey', align:'center', sortable: true},
			{ name: 'createDate', index:'createDate',align:'center', expType:'date',expValue:'yyyy-MM-dd',searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:'date',formatoptions: { srcformat: 'U', newformat: 'Y-m-d H:i:s' }},
			{ name: 'createDate', index:'createDate',align:'center', expType:'date',expValue:'yyyy-MM-dd',searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:'date',formatoptions: { srcformat: 'U', newformat: 'Y-m-d H:i:s' }}
        ],
        pager: "#taskPager",
        rowNum: 10,
        rowList: [10, 20, 30],
        sortname: "createDate",
        sortorder: "desc",
        viewrecords: true,
        gridview: true,
        autoencode: true,
        caption: "列表",
    	gridComplete: function(){
    		//设置隐藏/显示列字段
    		vue.taskCheckboxCols = [];
    		var gridColModel = taskGrid.getGridParam("colModel");
	    	var gridColNames = taskGrid.getGridParam("colNames");
	    	for(var i=0; i < gridColNames.length; i++){
	    		if(gridColNames[i].indexOf("role='checkbox'") == -1){
		    		var name = gridColNames[i];
					var value = gridColModel[i].name;
					var visible = true;
					if(gridColModel[i].hidden){
						visible = false;
					}
		    		vue.taskCheckboxCols.push({name:name, value:value, visible:visible});
	    		}
	    	}
    	}
    });
}

function defaultCondition(){
	var jobId = vue.jobId;
	var rules = [];
	rules.push({"field":"job.id","op":"cn","data":jobId});
	var filters = {"groupOp":"AND","rules":rules};
	
	return JSON.stringify(filters);
	
}

