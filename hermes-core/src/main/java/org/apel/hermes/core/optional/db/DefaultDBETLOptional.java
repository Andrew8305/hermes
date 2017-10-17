package org.apel.hermes.core.optional.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apel.gaia.commons.pager.Condition;
import org.apel.gaia.commons.pager.Operation;
import org.apel.gaia.commons.pager.RelateType;
import org.apel.hermes.core.common.DataSourceFetureDesc;
import org.apel.hermes.core.common.UpdateMode;
import org.apel.hermes.core.enums.InputPKType;
import org.apel.hermes.core.listener.ConvertHandler;

/**
 * 默认的数据库同步参数类，提供默认的数据库同步参数实现
 * @author lijian
 *
 */
public class DefaultDBETLOptional implements DBETLOptional {
	
	
	/**
	 * 是否进行数据版本检查，如果开启，每一次同步数据都会生成数据的新版本号，并且清楚掉以前老的版本号
	 * 此操作用于清除掉目标数据库中被源数据库删除的脏数据
	 */
	private boolean checkVersion;
	
	/**
	 * 数据版本检查的字段
	 */
	private String versionCheckField;
	
	/**
	 * 输入源查询的非条件，用于空查询，默认是where 1=2
	 */
	private String noneClause;
	
	/**
	 * 输入源表id的数据类型，默认是String
	 */
	private InputPKType inputPKType = InputPKType.STRING;
	
	/**
	 * 增强分页语句中的id过滤条件
	 */
	private String idFilterClause;
	
	/**
	 * 分页查找id的sql语句，默认是select inputPk from fromTable
	 */
	private String pagingSearchIdSql;
	
	/**
	 * 分页的线程池大小，默认10个线程
	 */
	private int pageThreadPoolSize;
	
	/**
	 * 分页时每查询的记录数，默认200一页
	 */
	private int rowsPerPage;
	
	/**
	 * 是否进行分页
	 */
	private boolean isPaging;
	
	/**
	 * 是否进行分页多线程数据提取，只有当参数isPaging为true的时候才能使用
	 */
	private boolean multiThreadPaging;
	
	/**
	 * 输入源要同步的表名
	 */
	private String fromTableName;
	
	/**
	 * 输出源要同步的表名
	 */
	private String toTableName;
	
	/**
	 * 提取数据的sql
	 */
	private String fromSql;
	
	/**
	 * 提取数据的统计总数的sql，只有当isPaging为true时才使用
	 */
	private String fromCountSql;
	
	/**
	 * 只有在isCheckTarget为true时才会启动
	 * 输出模式，输出模式有更新模式和删除模式，指的在数据存储的时候是以更新方式还是删除插入方式进行
	 */
	private UpdateMode updateMode = UpdateMode.DELETE;
	
	/**
	 * 在数据清洗环节额外增加的字段
	 */
	private Map<String,Object> addtionFields = new HashMap<String,Object>(); 
	
	/**
	 * 在数据清洗环节需要排除的字段
	 */
	private List<String> excludeFields = new ArrayList<>();
	
	/**
	 * 在数据清洗环节是否添加数据源的特性(可以添加数据源id，name或者两者都添加)
	 */
	private DataSourceFetureDesc dataSourceFetureDesc;
	
	/**
	 * 是否递归读取数据，用在读取自关联表时，递归读取只能单线程读取
	 */
	private boolean recursion;
	
	/**
	 * 递归读取数据时依据的字段
	 */
	private String recursionField;
	
	/**
	 * 递归读取数据时，最顶层节点数据的值
	 */
	private Object recursionFieldValue;
	
	/**
	 * 输入源主键字段，递归读取数据时候需要用到的
	 */
	private String inputPk;
	
	/**
	 * 输出源主键字段，递归删除目标数据库数据时候需要用到的
	 */
	private String outputPk;
	
	/**
	 * 是否检查输出源数据
	 */
	private boolean isCheckTarget;
	
	/**
	 * 检查输出源输出的条件
	 */
	private List<Condition> checkCondtions = new ArrayList<>();
	
	/**
	 * 在输出源检查时候，如果是删除模式，则是否进行递归删除---解决自关联表删除不掉的情况
	 */
	private boolean recursionDelete;
	
	/**
	 * 输出源检查时，递归删除的标识字段
	 */
	private String recursionDeleteField; 
	
	/**
	 * 输入和输出源的对照，用于转换输入源和输出源的的字段信息
	 */
	private Map<String, String> contrast = new HashMap<>();
	
	/**
	 * 数据转换回调接口
	 */
	private ConvertHandler convertHandler;
	
	@Override
	public UpdateMode updateMode() {
		return this.updateMode;
	}

	@Override
	public DBETLOptional updateMode(UpdateMode updateMode) {
		this.updateMode = updateMode;
		return this;
	}

	@Override
	public boolean isPaging() {
		return isPaging;
	}

	@Override
	public DBETLOptional isPaging(boolean isPaging) {
		this.isPaging = isPaging;
		return this;
	}

	@Override
	public boolean multiThreadPaging() {
		return this.multiThreadPaging;
	}

	@Override
	public DBETLOptional multiThreadPaging(boolean multiThreadPaging) {
		this.multiThreadPaging = multiThreadPaging;
		return this;
	}

	@Override
	public DBETLOptional fromTableName(String fromTableName) {
		this.fromTableName = fromTableName;
		return this;
	}

	@Override
	public String fromTableName() {
		return this.fromTableName;
	}

	@Override
	public DBETLOptional toTableName(String toTableName) {
		this.toTableName = toTableName;
		return this;
	}

	@Override
	public String toTableName() {
		return this.toTableName;
	}

	@Override
	public DBETLOptional fromSql(String fromSql) {
		this.fromSql = fromSql;
		return this;
	}

	@Override
	public String fromSql() {
		return this.fromSql;
	}

	@Override
	public DBETLOptional fromCountSql(String fromCountSql) {
		this.fromCountSql = fromCountSql;
		return this;
	}

	@Override
	public String fromCountSql() {
		return this.fromCountSql;
	}

	@Override
	public DBETLOptional addtionFields(Map<String, Object> addtionFields) {
		this.addtionFields = addtionFields;
		return this;
	}

	@Override
	public Map<String, Object> addtionFields() {
		return this.addtionFields;
	}

	@Override
	public DBETLOptional excludeFields(List<String> excludeFields) {
		this.excludeFields  = excludeFields;
		return this;
	}

	@Override
	public List<String> excludeFields() {
		return this.excludeFields;
	}

	@Override
	public DBETLOptional dataSourceFetureDesc(DataSourceFetureDesc dataSourceFetureDesc) {
		this.dataSourceFetureDesc = dataSourceFetureDesc;
		return this;
	}

	@Override
	public DataSourceFetureDesc dataSourceFetureDesc() {
		return this.dataSourceFetureDesc;
	}

	@Override
	public DBETLOptional recursion(boolean recursion) {
		this.recursion = recursion;
		return this;
	}

	@Override
	public boolean recursion() {
		return this.recursion;
	}

	@Override
	public DBETLOptional recursionField(String recursionField) {
		this.recursionField = recursionField;
		return this;
	}

	@Override
	public String recursionField() {
		return this.recursionField;
	}

	@Override
	public DBETLOptional recursionFieldValue(Object recursionFieldValue) {
		this.recursionFieldValue = recursionFieldValue;
		return this;
	}

	@Override
	public Object recursionFieldValue() {
		return this.recursionFieldValue;
	}

	@Override
	public DBETLOptional isCheckTarget(boolean isCheckTarget) {
		this.isCheckTarget = isCheckTarget;
		return this;
	}

	@Override
	public boolean isCheckTarget() {
		return this.isCheckTarget;
	}

	@Override
	public DBETLOptional checkCondtions(List<Condition> checkCondtions) {
		this.checkCondtions = checkCondtions;
		return this;
	}

	@Override
	public List<Condition> checkCondtions() {
		return this.checkCondtions;
	}

	@Override
	public DBETLOptional inputPk(String pk) {
		this.inputPk = pk;
		return this;
	}

	@Override
	public String inputPk() {
		return this.inputPk;
	}
	
	

	@Override
	public DBETLOptional checkPK() {
		isCheckTarget = true;
		Condition condition = new Condition();
		condition.setPropertyName(this.outputPk);
		condition.setRelateType(RelateType.AND);
		condition.setOperation(Operation.EQ);
		checkCondtions.add(condition);
		return this;
	}

	@Override
	public boolean recursionDelete() {
		return this.recursionDelete;
	}

	@Override
	public DBETLOptional recursionDelete(boolean recursionDelete) {
		this.recursionDelete = recursionDelete;
		return this;
	}

	@Override
	public String recursionDeleteField() {
		return this.recursionDeleteField;
	}

	@Override
	public DBETLOptional recursionDeleteField(String recursionDeleteField) {
		this.recursionDeleteField = recursionDeleteField;
		return this;
	}

	@Override
	public DBETLOptional outputPk(String outputPk) {
		this.outputPk = outputPk;
		return this;
	}

	@Override
	public String outputPk() {
		return this.outputPk;
	}

	@Override
	public Map<String, String> contrast() {
		return this.contrast;
	}

	@Override
	public DBETLOptional contrast(Map<String, String> contrast) {
		this.contrast = contrast;
		return this;
	}

	@Override
	public DBETLOptional convertHandler(ConvertHandler convertHandler) {
		this.convertHandler = convertHandler;
		return this;
	}

	@Override
	public ConvertHandler convertHandler() {
		return this.convertHandler;
	}

	@Override
	public int rowsPerPage() {
		return this.rowsPerPage;
	}

	@Override
	public DBETLOptional rowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
		return this;
	}

	@Override
	public int pageThreadPoolSize() {
		return this.pageThreadPoolSize;
	}

	@Override
	public DBETLOptional pageThreadPoolSize(int pageThreadPoolSize) {
		this.pageThreadPoolSize = pageThreadPoolSize;
		return this;
	}

	@Override
	public String pagingSearchIdSql() {
		return this.pagingSearchIdSql;
	}

	@Override
	public DBETLOptional pagingSearchIdSql(String pagingSearchIdSql) {
		this.pagingSearchIdSql = pagingSearchIdSql;
		return this;
	}

	@Override
	public String idFilterClause() {
		return this.idFilterClause;
	}

	@Override
	public DBETLOptional idFilterClause(String idFilterClause) {
		this.idFilterClause = idFilterClause;
		return this;
	}

	@Override
	public InputPKType inputPKType() {
		return this.inputPKType;
	}

	@Override
	public DBETLOptional inputPKType(InputPKType inputPKType) {
		this.inputPKType = inputPKType;
		return this;
	}

	@Override
	public String noneClause() {
		return this.noneClause;
	}
	
	@Override
	public DBETLOptional noneClause(String noneClause) {
		this.noneClause = noneClause;
		return this;
	}

	@Override
	public boolean checkVersion() {
		return this.checkVersion;
	}

	@Override
	public DBETLOptional checkVersion(boolean checkVersion) {
		this.checkVersion = checkVersion;
		return this;
	}

	@Override
	public String versionCheckField() {
		return this.versionCheckField;
	}

	@Override
	public DBETLOptional versionCheckField(String versionCheckField) {
		this.versionCheckField = versionCheckField;
		return this;
	}
	
	

	
}
