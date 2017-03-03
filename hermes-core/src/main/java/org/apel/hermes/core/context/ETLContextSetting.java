package org.apel.hermes.core.context;

/**
 * ETL上下文设置接口，所有job，task以及step接口是继承于该接口
 * 因为所有的job和task和step内部都会拥有一份context上下文的应用
 * @author lijian
 *
 */
public interface ETLContextSetting {

	void setContext(ETLContext context);
	
	ETLContext getContext();
	
}
