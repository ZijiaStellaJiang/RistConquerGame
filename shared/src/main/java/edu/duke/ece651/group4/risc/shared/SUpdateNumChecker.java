package edu.duke.ece651.group4.risc.shared;

public class SUpdateNumChecker<T> extends ActionRuleChecker<T>   {
    public SUpdateNumChecker(ActionRuleChecker<T> next) {super(next);}
	@Override
	protected String checkMyRule(ActionParser parse, Map<T> map, Player<T> p) {
        int level = parse.getLevel();
        int num = parse.getUnit();
        Integer ter_num = null;
        for(Territory<T> source : p.getMyTerritories()){
            if(source.getName().toUpperCase().equals(parse.getSource())){
                ter_num = source.getLevelUnitNum(level);
                break;
            }
        }
        if (ter_num == null || ter_num < num) {
            return "That action is invalid: not enough unit for upgrading to spy";
        }
        return null;
	}

}
