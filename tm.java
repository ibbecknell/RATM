import java.util.*;
public class tm {
	public static String[] registers = new String[10];
    public static String[] tape = new String[10];
    public static ArrayList<String> instructions = new ArrayList<String>();
	int k;

	public static void initRegs(){
		for(int s = 0; s < registers.length; s++){
			registers[s] = "0";
		}
	}
	public static void printRegs(){
		System.out.println("Registers:");
		int i = 0;
		for(String s : registers){
			System.out.println("\t"+i+": "+s);
			i++;
		}
	}
	public static void printTape(){
		System.out.println("Input Tape:");
		for(String s : tape){
			System.out.print("  "+s);
		}
		System.out.println();
	}
	
	public static void printInstList(){
		System.out.println("Instructions:");
		int i = 0;
		for(String s : instructions){
			System.out.println("\t"+i+": "+s);
			i++;
		}
	}
	
	public static boolean isHalt(String w){
		k = 0;
		return w.equals("halt");
	}
	public static boolean isRead(String w){
		return w.startsWith("read");
	}
	public static boolean isWrite(String w){
		return w.startsWith("write");
	}
	public static boolean isStore(String w){
		return w.startsWith("store");	
	}
	public static boolean isLoad(String w){
		return w.startsWith("load");
	}
	public static boolean isAdd(String w){
		return w.startsWith("add");
	}
	public static boolean isSub(String w){
		return w.startsWith("sub");
	}
	public static boolean ishalf(String w){
		return w.startsWith("half");
	}
	public static boolean isJump(String w){
		return w.startsWith("jump");
	}
	public static boolean isJpos(String w){
		return w.startsWith("jpos");
	}
	public static boolean isJzero(String w){
		return w.startsWith("jzero");
	}

	public static void updateTape(boolean read, String inst){
		String[] instList = inst.split(" ");
		int j = Integer.parseInt(instList[1]);
		int rj = Integer.parseInt(registers[j]);
		if(read){
			registers[0] = tape[rj];
		}else{
			tape[rj] = registers[0];
		}
		k++;
		return;
	}
	public static void updateRegs(String inst){
		// System.out.println("Update Regs");
		boolean isConst = false;
		String[] instList = inst.split(" ");

		int j;
		String operand = instList[1];
		if(operand.contains("=")){
			isConst = true;
			String op = instList[1].replace("=","");
			j = Integer.parseInt(op);
		}
		else{
			isConst = false;
			j = Integer.parseInt(instList[1]);
		}
		
		int rj = Integer.parseInt(registers[j]);

		if(isStore(inst)){
			registers[j] = registers[0];
		} else if(isLoad(inst)){
			if(isConst){
				registers[0] = Integer.toString(j);
			} else {
				registers[0] = registers[j];
			}
			k++;
		} else if(isAdd(inst)){
			if(isConst){
				System.out.println("constant");
				registers[0] = Integer.toString(j + Integer.parseInt(registers[0]));
			} else {
				registers[0] = Integer.toString(Integer.parseInt(registers[j]) + Integer.parseInt(registers[0]));
			}
			k++;
			System.out.println("after add k: "+k);
		} else if(isSub(inst)){
			int result; 
			if(isConst){
				result = Integer.parseInt(registers[0]) - j;
			} else {
				result = Integer.parseInt(registers[0]) - Integer.parseInt(registers[j]);
			}
			if(result<0){
				result = 0;
			}
			registers[0] = Integer.toString(result);
			k++;
		} else if(ishalf(inst)){
			int result = Integer.parseInt(registers[0])/2;
			registers[0] = Integer.toString(result);
			k++;
		} else if(isJump(inst)){
			k = j;
		} else if(isJpos(inst)){
			int rz = Integer.parseInt(registers[0]);
			if(rz > 0){
				k = j;
			}
		} else if(isJzero(inst)){
			int rz = Integer.parseInt(registers[0]);
			if(rz == 0){
				k = j;
			}
		}
		
		return;
	}
	public static void executeInst(String inst){
		System.out.println("k: "+k);
		if(isRead(inst) || isWrite(inst)){
			updateTape(isRead(inst),inst);
		}else if(isHalt(inst)){
			System.out.println("HALT");
			k = 0;
			return;
		}else{
			updateRegs(inst);
		}
	}
	public static void executeList(int k){
		while(k != 0){
			String inst = instructions.get(k);
			executeInst(inst);
		}
		return;
	}
	public static void getInput(){
		System.out.println("enter Input word");
    	Scanner scan = new Scanner(System.in);
    	String elem = scan.next();
    	tape = elem.split("");
    	printTape();
    	instructions.add(null);
		System.out.println("Enter Instructions");
		while(scan.hasNext()){
    		String inst = scan.nextLine();
    		if(!inst.isEmpty()){
    			instructions.add(inst);
    		}
    		if(inst.equals("halt")){
    			break;
    		}
    	}
    	scan.close();
	}

    public static void main(String args[]) {
    	tm.k = 1;
    	initRegs();
    	
	   	getInput();

    	executeList(tm.k);
    	
        printRegs();
    	// printInstList();
    }
}