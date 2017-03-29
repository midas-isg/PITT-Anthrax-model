package edu.pitt.rods.apollo;

import java.sql.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import edu.pitt.rods.apollo.epidemicmodels.ModelMetaData;
import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.CompartmentEpiModelGlobalResult;
import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.util.ObjectStringSerializer;

/**
 * Provides a framework for converting a delimited parameter string into a
 * HashMap where the HashMap.key is the name of the parameter and the
 * HashMap.value is an instantiated object representation of the value passed in
 * as a string. <p> The format of the delimited parameter string is as follows:
 * key:type=value;key:type=value;key:type=value;etc... <p> As of 2/18/2010 the
 * class understands the following "type"s: boolean integer double long string
 * <p> To use the class, call the constructor with the delimited parameter
 * string. The constructor will call the unpack() method which will populate the
 * HashMap. Access the HashMap via the getters provided. <p>
 *
 * @author John Levander
 *
 * TODO: Rename the class. I use this class in it's below state to unpack string
 * delimited parameters. Therefore, it doesn't need to be a "Component".
 */
public class ParameterizedComponent extends ModelMetaData implements
        java.io.Serializable {

    private static final long serialVersionUID = -5836742450384726862L;
    static Logger logger = Logger.getLogger(ParameterizedComponent.class);
    private HashMap<String, Object> parameters = new HashMap<String, Object>();
    private String args = "";
    public static final String PARAM_1D_INT_ARRAY = "1dintarray";
    public static final String PARAM_1D_DOUBLE_ARRAY = "1ddblarray";
    public static final String PARAM_1D_PRIMITIVE_DOUBLE_ARRAY = "1dprimdblarray";

    /**
	 * Constructor is responsible to populating the parameters HashMap by
	 * calling unpack();
	 * 
	 * @param args
	 *            A delimited parameter string in the format:
	 *            "key:type=value;key:type=value"
	 */
    public ParameterizedComponent(String args) {
        // logger.debug("Constructor called with " + args);
        this.args = args;
        unpack();
    }

    /**
	 * The unpack() method reads the "args" member variable, which is a
	 * delimited parameter string set in the constructor, and converts the
	 * variables defined in the parameter string into Objects. These Objects are
	 * then stored in the parameters HashMap, along with the name of each
	 * parameter.
	 */
    protected void unpack() {
        if (args.length() == 0) {
            return;
        }
        String[] entries = args.split(";");
        for (String entry : entries) {
            String name = entry.split(":")[0];
            String type = entry.split(":")[1].split("=")[0];
            String value = "";
            if (entry.endsWith("=") && (!type.equalsIgnoreCase("os"))) {
                value = "";
            } else {
                value = entry.split("=")[1];
            }

            if (type.equalsIgnoreCase("double")) {
                setParam(name, Double.valueOf(value));
                continue;
            }
            if (type.equalsIgnoreCase("string")) {
                setParam(name, (value));
                continue;
            }
            if (type.equalsIgnoreCase("integer")) {
                setParam(name, Integer.valueOf(value));
                continue;
            }
            if (type.equalsIgnoreCase("boolean")) {
                setParam(name, Boolean.valueOf(value));
                continue;
            }
            if (type.equalsIgnoreCase("long")) {
                setParam(name, Long.valueOf(value));
                continue;
            }
            if (type.equalsIgnoreCase("date")) {
                setParam(name, Date.valueOf(value));
                continue;
            }

            if (type.equalsIgnoreCase("os")) {
                setParam(name,
                         (CompartmentEpiModelGlobalResult) ObjectStringSerializer.deserializeStringToObject(value));
            }

            if (type.equalsIgnoreCase(PARAM_1D_INT_ARRAY)) {
                setParam(name, get1dIntArray(value));
            }
            if (type.equalsIgnoreCase(PARAM_1D_DOUBLE_ARRAY)) {
                setParam(name, get1dDoubleArray(value));
            }
            if (type.equalsIgnoreCase(PARAM_1D_PRIMITIVE_DOUBLE_ARRAY)) {
                setParam(name, get1ddoubleArray(value));
            }

        }
    }

    // must begin and end with a square bracket
    private boolean validate1dIntArray(String value) {
        if (value.startsWith("[") && value.endsWith("]")) {
            return true;
        } else {
            return false;
        }
    }

    // [2,3,2,3]
    private int[] get1dIntArray(String value) {
        // just make sure the string is in the right format
        if (validate1dIntArray(value)) {
            if (value.equalsIgnoreCase("[]")) {
                return new int[0];
            }
            value = value.substring(1, value.length() - 1);
            String strArray[] = value.split(",");
            int result[] = new int[strArray.length];
            for (int i = 0; i < strArray.length; i++) {
                String origStr = strArray[i];
                String trimmedStr = "";
                for (int j = 0; j < origStr.length(); j++) {
                    if (origStr.charAt(j) != ' ') {
                        trimmedStr += origStr.charAt(j);
                    }
                }
                result[i] = Integer.valueOf(trimmedStr);
            }
            return result;
        }
        return null;
    }

    private Double[] get1dDoubleArray(String value) {
        // just make sure the string is in the right format
        if (validate1dIntArray(value)) {
            if (value.equalsIgnoreCase("[]")) {
                return new Double[0];
            }
            value = value.substring(1, value.length() - 1);
            String strArray[] = value.split(",");
            Double result[] = new Double[strArray.length];
            for (int i = 0; i < strArray.length; i++) {
                String origStr = strArray[i];
                String trimmedStr = "";
                for (int j = 0; j < origStr.length(); j++) {
                    if (origStr.charAt(j) != ' ') {
                        trimmedStr += origStr.charAt(j);
                    }
                }
                result[i] = Double.valueOf(trimmedStr);
            }
            return result;
        }
        return null;
    }

    private double[] get1ddoubleArray(String value) {
        // just make sure the string is in the right format
        if (validate1dIntArray(value)) {
            if (value.equalsIgnoreCase("[]")) {
                return new double[0];
            }
            value = value.substring(1, value.length() - 1);
            String strArray[] = value.split(",");
            double result[] = new double[strArray.length];
            for (int i = 0; i < strArray.length; i++) {
                String origStr = strArray[i];
                String trimmedStr = "";
                for (int j = 0; j < origStr.length(); j++) {
                    if (origStr.charAt(j) != ' ') {
                        trimmedStr += origStr.charAt(j);
                    }
                }
                result[i] = Double.valueOf(trimmedStr);
            }
            return result;
        }
        return null;
    }

    @Override
    public String toString() {
        return args;
    }

    public boolean parameterExists(String name) {
        return parameters.containsKey(name);
    }

    // Only getters and setters below this line//
    public Boolean getBooleanParam(String name) {
        return (Boolean) parameters.get(name);
    }

    public Double getDoubleParam(String name) {
        return (Double) parameters.get(name);
    }

    public Integer getIntegerParam(String name) {
        return (Integer) parameters.get(name);
    }

    public Long getLongParam(String name) {
        return (Long) parameters.get(name);
    }

    public String getStringParam(String name) {
        return (String) parameters.get(name);
    }

    public CompartmentEpiModelGlobalResult getCompartmentEpiModelGlobalResult(
            String name) {
        return (CompartmentEpiModelGlobalResult) parameters.get(name);
    }

    public int[] get1DIntArrayParam(String name) {
        return (int[]) parameters.get(name);
    }

    public Double[] get1DDoubleArrayParam(String name) {
        return (Double[]) parameters.get(name);
    }

    public double[] get1DdoubleArrayParam(String name) {
        return (double[]) parameters.get(name);
    }

    public void setParam(String name, Boolean value) {
        logger.debug("Boolean parameter created " + name + "=" + value);
        parameters.put(name, value);
    }

    public void setParam(String name, Date value) {
        logger.debug("Date parameter created " + name + "=" + value);
        parameters.put(name, value);
    }

    public void setParam(String name, Double value) {
        logger.debug("Double parameter created " + name + "=" + value);
        parameters.put(name, value);
    }

    public void setParam(String name, Integer value) {
        logger.debug("Integer parameter created " + name + "=" + value);
        parameters.put(name, value);
    }

    public void setParam(String name, Long value) {
        logger.debug("Long parameter created " + name + "=" + value);
        parameters.put(name, value);
    }

    public void setParam(String name, String value) {
        logger.debug("String parameter created " + name + "=" + value);
        parameters.put(name, value);
    }

    public void setParam(String name, CompartmentEpiModelGlobalResult value) {
        logger.debug("CompartmentEpiModelGlobalResult created.");
        parameters.put(name, value);
    }

    public void setParam(String name, int[] value) {
        logger.debug("Integer Array created.");
        parameters.put(name, value);
    }

    public void setParam(String name, Double[] value) {
        logger.debug("Integer Array created.");
        parameters.put(name, value);
    }

    public void setParam(String name, double[] value) {
        logger.debug("Integer Array created.");
        parameters.put(name, value);
    }
}
