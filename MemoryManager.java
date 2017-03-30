package cse2010.assignment2;

import cse2010.assignment2.DLinkedList;

/* Block will be used as a type argument */
class Block {
    public int size;
    public int start;
    public int end;

    public Block(int size, int start, int end) {
        this.size = size;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "(" + size +", " + start + ", " + end + ")";
    }
}

public class MemoryManager {

    private DLinkedList<Block> heap = new DLinkedList<>();
    private DLinkedList<Block> use = new DLinkedList<>();

    public MemoryManager(int capacity) {
       /**/
    	if(capacity < 0) throw new OutOfMemoryException("Only Positive Interger.");
    	Node<Block> freeStore = new Node<Block>(new Block(capacity, 0, capacity-1), null, null);
    	heap.addFirst(freeStore);
    }
  
    
    public Block malloc(int size) throws OutOfMemoryException {
		/**/
    	while(size > 0){    		
    		if(FindSizeNode(size) == null || canUseCapacity() < size){
    			throw new OutOfMemoryException("Out of Memory");
    		}
    		Block tem = FindSizeNode(size).getItem();
    		int usize = tem.size - size;
    		if(usize == 0) heap.remove(FindSizeNode(size));
    		int ustart = tem.start + size;
    		setBlock(tem, usize , ustart, tem.end);
    		Block useBlock = new Block(size, tem.start-size, tem.start-1);
    		use.addLast(new Node<Block>(useBlock, null, null));
    		return useBlock;
    	}
    	throw new OutOfMemoryException("Only Positive Interger.");
    }

    public void free(Block block) {
        /**/
    	while(block.size + block.start-1 != block.end){
    		throw new OutOfMemoryException("wrong block");}
    	Node<Block> temp = Find(use, block);
    	if(temp == null) throw new OutOfMemoryException("It is not a malloc block. Use only allocated blocks.");
    	use.remove(temp);
    	if(heap.getSize() == 0){
    		Node<Block> free = new Node<Block>(block, null, null);
    		heap.addLast(free);
    	}else{
    		Node<Block> tempblock = heap.getFirst();
    		int count = 0;
    		while(count == 0){
    			count = Merge(tempblock, block) + count;
    			if(tempblock == heap.getLast()) break;
    			tempblock = tempblock.getNext();
    		}
    		if(count == 0){
    			Node<Block> free = new Node<Block>(block, null, null);
    			try {
    				heap.addAfter(FindPlace(block),free);
				} catch (Exception e) {
					throw new OutOfMemoryException("Wrong Index");
				}					
    		}else if(count > 1){
    			throw new OutOfMemoryException("merge Error");
    		}
    	}
    }

    // for debugging purpose only
    public DLinkedList<Block> getHeap() {
        return heap;
    }
    
    @Override
    public String toString() {
        return heap.toString();
    }  
    
    private int canUseCapacity(){
    	int allamount = 0;
    	Node<Block> temp = heap.getFirst();
    	for(int i = 0; i< heap.getSize(); i++){
    		allamount += temp.getItem().size;
    		temp = temp.getNext();
    	}return allamount;
    }
    private void setBlock(Block tem, int size, int start, int end){
    	tem.size = size;
    	tem.start = start;
    	tem.end = end;
    }
    private Node<Block> FindSizeNode(int size){
    	if(heap.getSize() == 0) return null;
    	Node<Block> temp = heap.getFirst();
    	for(int i=0; i < heap.getSize(); i++){
			if(temp.getItem().size >= size){
				return temp;}
			else temp = temp.getNext();
    
    	}return null;
    }
    
    private int Merge(Node<Block> citizen, Block stranger){
    	int ustart = citizen.getItem().start;
    	int uend = citizen.getItem().end;
    	int tempsize = stranger.size + citizen.getItem().size;
    	boolean uppermerge = ustart - stranger.end == 1;
    	boolean downmerge = stranger.start - uend == 1;
    	while(citizen == heap.getLast()){
    		if(uppermerge){
    			setBlock(citizen.getItem(), tempsize , stranger.start, uend);
    			return 1;
    		}else if(downmerge){
    			setBlock(citizen.getItem(), tempsize , ustart, stranger.end);
    			return 1;
    		}else return 0;
    	}
    	if(downmerge & citizen.getNext().getItem().start - stranger.end == 1){
    		int triplesize = tempsize + citizen.getNext().getItem().size; 
    		setBlock(citizen.getItem(), triplesize, ustart, citizen.getNext().getItem().end);
    		heap.remove(citizen.getNext());
    		return 1;
    	}
    	else if(uppermerge){
    		setBlock(citizen.getItem(), tempsize , stranger.start, uend);
    		return 1;
    	}
    	else if(downmerge){
    		setBlock(citizen.getItem(), tempsize , ustart, stranger.end);
    		return 1;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
    	}else return 0;
    }
    private Node<Block> FindPlace(Block block){
    	Node<Block> temp = heap.getFirst();
   		if(block.end < temp.getItem().start) return temp.getPrev();
   		while(heap.getSize() == 1){
   	   		if(block.end < temp.getItem().start) return temp.getPrev();
   	   		else if(temp.getItem().end < block.start) return temp;
   	   		else return null;
   		}
    	while(true){
    		if(temp == heap.getLast()){
    			if(temp.getItem().end < block.start) return temp;
    			else if(block.end < temp.getItem().start && temp.getPrev().getItem().end < block.start){
    				return temp.getPrev();}
    			else return null;
    		}
    		else if(block.end < temp.getNext().getItem().start && temp.getItem().end < block.start){
    			return temp;}
    		temp = temp.getNext();
    	}
    }
    private Node<Block> Find(DLinkedList<Block> wonder, Block block){
    	if(wonder.getSize() == 0) return null;
    	Node<Block> node = wonder.getFirst();
		while(true){
			if(block.size == node.getItem().size && block.start == node.getItem().start && block.end == node.getItem().end){
				return node;
			}
			if(node == wonder.getLast()) break;
			node = node.getNext();
		}return null;
    }
}



