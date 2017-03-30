package cse2010.assignment2;

import java.util.Random;
import java.util.Scanner;

class test{
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("설정 용량 : ");
		int capa = sc.nextInt();
		MemoryManager m = new MemoryManager(capa);
		MemoryManager use = new MemoryManager(capa);
		use.getHeap().remove(use.getHeap().getFirst());
		while(true){
			System.out.print("\n(Exit, Malloc, free) = (0,1,2) : ");
			int also = sc.nextInt();
			if(also == 0) break;
			if(also == 1){
				System.out.println("필요한 용량을 입력하세요");
				int capa2 = sc.nextInt();
				Block block = m.malloc(capa2);
				Node<Block> node = new Node<Block>(block, null, null);
				use.getHeap().addFirst(node);
				System.out.println("store의 정보 :\t" + m);
				System.out.println("use의 정보 : \t" + use);
				
			}else if(also == 2){
				System.out.print("반환할 size값 : ");
				int size = sc.nextInt();
				System.out.print("반환할 start : ");
				int start = sc.nextInt();
				System.out.print("반환할 end값 : ");
				int end = sc.nextInt();
				Node<Block> node = null;
				Block block = new Block(size, start, end);
				m.free(block);
				node = use.getHeap().getFirst();
				while(true){
					if(size == node.getItem().size &&start == node.getItem().start && end == node.getItem().end){
						use.getHeap().remove(node);
						break;
					}
					if(node == use.getHeap().getLast()) break;
					node = node.getNext();
				}
				System.out.println("store의 정보 :\t" + m);
				System.out.println("use의 정보 : \t" + use);
				
			}
			
		}System.out.println("프로그램 끝!");
		
	}
}


