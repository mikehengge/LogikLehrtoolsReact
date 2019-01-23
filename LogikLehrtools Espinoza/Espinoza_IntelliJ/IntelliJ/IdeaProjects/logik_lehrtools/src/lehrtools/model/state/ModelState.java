package lehrtools.model.state;

/**
 * Set of Constants used to define states an actions used in the application.
 */
public enum ModelState {
	START ("Start"),
	RESOLUTION ("Resolution"),
	RESOLUTION_OVER ("Resolution über "),
	BEFORE_RESOLUTION ("Before Resolution"),
	SUBSUMPTION_FW ("Forward Subsumption"),
	SUBSUMPTION_BW ("Backward Subsumption"),
	SUBSUMPTION ("Subsumption"),
	BEFORE_SUBSUMPTION ("Before Subsumption"),
	END ("Ende"),
	SHOW_TREE("Ableitungsbaum"),
	HIDE_TREE("Ableitungsbaum"),
	BACK ("Back"),
	RULE_01("Regel 1 UC"),
	RULE_01_OVER("Regel 1 UC über "),
	RULE_02("Regel 2 PL"),
	RULE_02_OVER("Regel 2 PL über "),
	RULE_03("Regel 3 VE"),
	SHOW_CALCULATION("Berrechnungen");

	private final String _name;

	/**
	 * Private constructor that adds String field to each element
	 * of the enumeration.
	 * @param name string representation of the constant.
	 */
	private ModelState(String name)
	{
		_name = name;
	}
	
	@Override
	public String toString()
	{
		return _name;
	}
}
