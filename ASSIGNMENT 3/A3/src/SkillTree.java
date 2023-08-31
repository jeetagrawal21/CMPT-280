/**
 * NAME			:		JEET AGRAWAL
 * NSID			:		jea316
* STUDENT ID	:		11269096
 */

import lib280.exception.ContainerEmpty280Exception;
import lib280.list.LinkedIterator280;
import lib280.list.LinkedList280;
import lib280.tree.BasicMAryTree280;
import lib280.tree.MAryNode280;

import java.lang.invoke.ConstantBootstraps;
import java.util.LinkedList;

public class SkillTree extends BasicMAryTree280<Skill> {

	/**	
	 * Create lib280.tree with the specified root node and
	 * specified maximum arity of nodes.  
	 * @timing O(1) 
	 * @param x item to set as the root node
	 * @param m number of children allowed for future nodes 
	 */
	public SkillTree(Skill x, int m)
	{
		super(x,m);
	}

	/**
	 * A convenience method that avoids typecasts.
	 * Obtains a subtree of the root.
	 * 
	 * @param i Index of the desired subtree of the root.
	 * @return the i-th subtree of the root.
	 */
	public SkillTree rootSubTree(int i) {
		return (SkillTree)super.rootSubtree(i);
	}

	/**
	 * A method which returns True if the skill exists in the given tree and false otherwise,
	 * If it exists, it also adds it to a LinkedList280.
	 * @param treeNode Its a node of MAryNode280<Skill>
	 * @param skillName Its the name of the string which is to be looked for.
	 * @param linkedList The LinkedList which stores the path.
	 * @return a boolean , true if path exists and false otherwise.
	 */
	public boolean hasPath(MAryNode280<Skill> treeNode , String skillName , LinkedList280<Skill> linkedList) {
		boolean flag;

		if (treeNode == null) return false;

		else if ((treeNode.item().getSkillName().equals(skillName))) {
			linkedList.insert(treeNode.item());
			return true;
		}
		else{
			for (int i = 1; i <= treeNode.count() ; i++)
			{
				/**
				 * Redundant code removed after submitting assignment.
				 * Silly me didn't even think of it before.
				 */
//				if ((treeNode.item().getSkillName()).equals(skillName))
//				{
//					linkedList.insert(treeNode.item());
//					return true;
//				}
//				else
					{
						flag = hasPath(treeNode.subnode(i),skillName,linkedList);
							if (flag)
							{
								linkedList.insert(treeNode.item());
								return true;
							}
					}
			}
		}
		return false;
	}

	/**
	 * Returns a LinkedList<280> which stores the path or the prerequisites for the given skill.
	 * @param skill_name Name of the string for which we need to find the prerequisite skills.
	 * @return A LinkedList<280> which stores the path or the prerequisites for the given skill.
	 * @throws RuntimeException In case the Skill doesn't exist.
	 */
	public LinkedList280<Skill> skillDependencies(String skill_name) throws RuntimeException {

		LinkedList280<Skill> preRequisites = new LinkedList280<Skill>();

		if (hasPath(this.rootNode,skill_name,preRequisites)){
			return preRequisites;
		}
		else throw new RuntimeException("Did not found the given skill.");
	}

	/**
	 * Returns the total cost for the given skill to be added with including the costs of the prerequisite skill costs.
	 * @param skillName Name of the string for which we need to find the total cost.
	 * @return The total cost for the given skill to be added with including the costs of the prerequisite skill costs.
	 */
	public int skillTotalCost(String skillName){

		LinkedList280<Skill> preReqList = skillDependencies(skillName);

		int sum = 0;

		preReqList.goFirst();
		while(!preReqList.after()){
			sum += preReqList.item().getSkillCost();
			preReqList.goForth();
		}
		return sum;
	}

	public static void main(String[] args) {

		Skill toWalk = new Skill("Walker","Helps you to walk.",1);

		Skill toRun = new Skill("Runner","Helps you to run.",2);
		Skill toJump = new Skill("Jumper","Helps you to jump.",2);
		Skill toWalkBack = new Skill("Back-Walker","Helps you to walk back.",4);

		Skill toSprint = new Skill("Sprinter","Helps you to sprint.",7);
		Skill toRunBack = new Skill("Back-Runner", "Helps you to run backwards.",10);

		Skill toClimb = new Skill("Climber","Helps you to climb.",10);
		Skill toLongJump = new Skill("LongJumper","Helps you to do a Long Jump.",10);
		Skill toHighJump = new Skill("HighJumper","Helps you to do a High Jump",10);
		Skill toBackFlip = new Skill("Back-Flipper","Helps you to do a Back Flip.",15);

		SkillTree skillTree_ROOT = new SkillTree(toWalk,3);

		SkillTree skillTree_1 = new SkillTree(toRun,2);
		SkillTree skillTree_2 = new SkillTree(toJump,4);
		SkillTree skillTree_3 = new SkillTree(toWalkBack,0);

		SkillTree skillTree_1_1 = new SkillTree(toSprint,0);
		SkillTree skillTree_1_2 = new SkillTree(toRunBack,0);

		SkillTree skillTree_2_1 = new SkillTree(toClimb,0);
		SkillTree skillTree_2_2 = new SkillTree(toLongJump,0);
		SkillTree skillTree_2_3 = new SkillTree(toHighJump,0);
		SkillTree skillTree_2_4 = new SkillTree(toBackFlip,0);

		skillTree_ROOT.setRootSubtree(skillTree_1,1);
		skillTree_ROOT.setRootSubtree(skillTree_2,2);
		skillTree_ROOT.setRootSubtree(skillTree_3,3);

		skillTree_1.setRootSubtree(skillTree_1_1,1);
		skillTree_1.setRootSubtree(skillTree_1_2,2);

		skillTree_2.setRootSubtree(skillTree_2_1,1);
		skillTree_2.setRootSubtree(skillTree_2_2,2);
		skillTree_2.setRootSubtree(skillTree_2_3,3);
		skillTree_2.setRootSubtree(skillTree_2_4,4);

		System.out.println(skillTree_ROOT.toStringByLevel());
		System.out.println();
///	THIS POINT STATES THE END OF Q2 PART A.

		// TESTING FOR skillDependencies.

		// FOR THE SKILLS WHICH DO EXIST.

		String skill_name = "Climber"; // Name of the skill

		LinkedList280<Skill> test_1 = skillTree_ROOT.skillDependencies(skill_name);
		String result_1 = "Walker, Cost: 1, Jumper, Cost: 2, Climber, Cost: 10, ";
		System.out.println("Dependencies for "+skill_name+" :\n");
		System.out.println(test_1.toString());

		if (!result_1.equals(test_1.toString())){
			System.out.println("Error: The path is incorrect.");
		}

		skill_name = "Back-Flipper";
		LinkedList280<Skill> test_2 = skillTree_ROOT.skillDependencies(skill_name);
		String result_2 = "Walker, Cost: 1, Jumper, Cost: 2, Back-Flipper, Cost: 15, ";
		System.out.println("Dependencies for "+skill_name+" :\n");

		System.out.println(test_2.toString());

		if (!result_2.equals(test_2.toString())){
			System.out.println("Error: The path is incorrect.");
		}

		skill_name = "Back-Walker";
		LinkedList280<Skill> test_3 = skillTree_ROOT.skillDependencies(skill_name);
		String result_3 = "Walker, Cost: 1, Back-Walker, Cost: 4, ";
		System.out.println("Dependencies for "+skill_name+" :\n");
		System.out.println(test_3.toString());
		System.out.println();

		if (!result_3.equals(test_3.toString())) {
			System.out.println("Error: The path is incorrect.");
		}

		// TESTING FOR THE SKILLS WHICH DOESN'T EXIST IN THE TREE.

		skill_name = "The-Flyer";
		try{
			System.out.println(skillTree_ROOT.skillDependencies(skill_name));
		}
		catch (RuntimeException e){
			System.out.println("Skill not found in the tree.");
		}

		skill_name = "The-Swimmer";
		try{
			System.out.println(skillTree_ROOT.skillDependencies(skill_name));
		}
		catch (RuntimeException e){
			System.out.println("Skill not found in the tree.");
		}

		// TESTING FOR TOTAL COST.

		// FOR SKILLS WHICH DO EXIST.

		skill_name = "Climber";
		int test_4 = skillTree_ROOT.skillTotalCost(skill_name);
		int result_4 = 13;
		System.out.println("Total Cost for "+skill_name+" :\n");
		System.out.println(test_4);

		if (test_4 != result_4){
			System.out.println("Error: The total cost shouldn't be "+test_4+" but "+result_4);
		}

		skill_name = "Walker";
		int test_5 = skillTree_ROOT.skillTotalCost(skill_name);
		int result_5 = 1;
		System.out.println("Total Cost for "+skill_name+" :\n");
		System.out.println(test_5);

		if (test_5 != result_5){
			System.out.println("Error: The total cost shouldn't be "+test_5+" but "+result_5);
		}

		skill_name = "LongJumper";
		int test_6 = skillTree_ROOT.skillTotalCost(skill_name);
		int result_6 = 13;
		System.out.println("Total Cost for "+skill_name+" :\n");
		System.out.println(test_6);

		if (test_6 != result_6){
			System.out.println("Error: The total cost shouldn't be "+test_6+" but "+result_6);
		}
		System.out.println();

		// FOR SKILLS WHICH DOESN'T EXIST.

		skill_name = "The-Swimmer";

		try{
			System.out.println(skillTree_ROOT.skillTotalCost(skill_name));
		}
		catch (RuntimeException e){
			System.out.println("Skill not found in the tree.");
		}

		skill_name = "The-Flyer";
		try{
			System.out.println(skillTree_ROOT.skillTotalCost(skill_name));
		}
		catch (RuntimeException e){
			System.out.println("Skill not found in the tree.");
		}

		System.out.println(skillTree_ROOT.skillDependencies("HighJumper"));
	}
}
