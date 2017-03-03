package org.apel.hermes.core.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apel.gaia.commons.pager.Condition;
import org.apel.gaia.commons.pager.Operation;
import org.apel.gaia.commons.pager.RelateType;
import org.apel.hermes.core.optional.db.DBETLOptional;
import org.apel.hermes.core.resource.db.DBETLResource;
import org.springframework.util.StringUtils;

/**
 * 同步逻辑中的检查目标源数据的条件
 * 
 * @author lijian
 *
 */
public class CheckCondition {

	public static class CheckResult {
		String whereClause;
		List<Object> values;
		public String getWhereClause() {
			return whereClause;
		}

		public void setWhereClause(String whereClause) {
			this.whereClause = whereClause;
		}

		public List<Object> getValues() {
			return values;
		}

		public void setValues(List<Object> values) {
			this.values = values;
		}
	}

	public static CheckResult checkSql(DBETLResource outputResource,
			DBETLOptional dbOptional, Map<String, Object> singleNewData) {
		CheckResult result = new CheckResult();
		// 根据现在转换完成之后的数据对condition进行赋值，使其形成一个where语句，用于对目标源进行操作
		List<Condition> conditions = new ArrayList<>();
		for (Condition condition : dbOptional.checkCondtions()) {
			Condition c = new Condition();
			c.setPropertyName(condition.getPropertyName());
			c.setRelateType(condition.getRelateType());
			c.setOperation(condition.getOperation());
			if (condition.getPropertyValue() == null) {
				c.setPropertyValue(singleNewData.get(condition
						.getPropertyName()));
			}
			conditions.add(c);
		}
		List<Object> values = new ArrayList<>();
		result.setWhereClause(" WHERE 1=1 "
				+ preCondition(conditions, values));
		result.setValues(values);
		return result;
	}

	public static String preCondition(List<Condition> conditions,
			List<Object> values) {
		StringBuffer c = new StringBuffer();
		if (conditions != null && conditions.size() > 0) {
			c.append(RelateType.AND.toString() + " ( ");
			for (int i = 0; i < conditions.size(); i++) {
				Condition condition = conditions.get(i);
				String groupPrefixBrackets = condition.getGroupPrefixBrackets();
				String propertyName = condition.getPropertyName();
				Object value = condition.getPropertyValue();
				boolean isPrefixBrackets = condition.isPrefixBrackets();
				boolean isSuffixBrackets = condition.isSuffixBrackets();
				Operation operation = condition.getOperation();
				RelateType relateType = condition.getRelateType();
				String related = "";
				if (i != 0) {
					if (relateType == null) {
						relateType = RelateType.AND;
					}
					related = relateType
							+ (isPrefixBrackets ? StringUtils.isEmpty(condition
									.getPreffixBracketsValue()) ? " ( " : " "
									+ condition.getPreffixBracketsValue() + " "
									: " ");
				} else {
					related = ""
							+ (isPrefixBrackets ? StringUtils.isEmpty(condition
									.getPreffixBracketsValue()) ? " ( " : " "
									+ condition.getPreffixBracketsValue() + " "
									: " ");
				}
				c.append(groupPrefixBrackets);
				switch (operation) {
				case NC:
				case CN:
					String[] list = value.toString().split("[, ]");
					if (list.length > 1) {
						c.append(related + " ( " + propertyName + operation
								+ "?");
						values.add("%" + list[0] + "%");
						for (int j = 1; j < list.length; j++) {
							c.append(RelateType.OR + propertyName + operation
									+ "?");
							values.add("%" + list[j] + "%");
						}
						c.append(" ) ");
					} else {
						c.append(related + propertyName + operation + "?");
						values.add("%" + value + "%");
					}
					break;
				case BN:
				case BW:
					c.append(related + propertyName + operation + "?");
					values.add(value + "%");
					break;
				case EN:
				case EW:
					c.append(related + propertyName + operation + "?");
					values.add("%" + value);
					break;
				case BETWEEN:
					c.append(related + propertyName + operation + "?" + " AND "
							+ "?");
					Object[] params = new Object[2];
					if (value instanceof String) {
						String[] array = value.toString().split("#|,");
						params[0] = array[0];
						params[1] = array[1];
					} else {
						params = (Object[]) value;
					}
					values.add(params[0]);
					values.add(params[1]);
					break;
				case NI:
				case IN:
					c.append(related + propertyName + operation + "(");
					if (value != null) {
						Class<?> clazz = value.getClass();
						if (clazz.isArray()) {
							Object[] array = (Object[]) value;
							for (Object object : array) {
								c.append("?,");
								values.add(object);
							}
							if (array.length > 0) {
								c.replace(c.length() - 1, c.length(), "");
							}
						} else if (value instanceof Collection<?>) {
							Collection<?> coll = (Collection<?>) value;
							for (Object object : coll) {
								c.append("?,");
								values.add(object);
							}
							if (coll.size() > 0) {
								c.replace(c.length() - 1, c.length(), "");
							}
						} else if (value instanceof String) {
							if (StringUtils.isEmpty((String) value)) {
								c.append("NULL");
							} else {
								String[] array = ((String) value).split(",");
								for (String val : array) {
									c.append("?,");
									values.add(val);
								}
								if (array.length > 0) {
									c.replace(c.length() - 1, c.length(), "");
								}
							}
						}
					} else {
						c.append("NULL");
					}
					c.append(")");
					break;
				case EQ:
				case GE:
				case GT:
				case LE:
				case LT:
				case NE:
					c.append(related + propertyName + operation + "?");
					values.add(value);
					break;
				case NN:
				case NU:
					c.append(related + propertyName + operation);
					break;
				default:
					break;
				}
				c.append(isSuffixBrackets ? StringUtils.isEmpty(condition
						.getSuffixBracketsValue()) ? " ) " : " "
						+ condition.getSuffixBracketsValue() + " " : " ");
			}
			c.append(" ) ");
		}
		return c.toString();
	}

}
