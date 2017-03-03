package org.apel.hermes.core.factory.db;

import org.apel.hermes.core.factory.SmartStepFactory;
import org.apel.hermes.core.step.db.SmartDBETLStep;
import org.springframework.stereotype.Component;

/**
 * 数据库智能step创建工厂
 * @author lijian
 *
 */
@Component
public class SmartDBStepFactory implements SmartStepFactory<SmartDBETLStep> {

	@Override
	public SmartDBETLStep createSmartStep(int order) {
		SmartDBETLStep step = new SmartDBETLStep();
		step.order(order);
		return step;
	}

}
