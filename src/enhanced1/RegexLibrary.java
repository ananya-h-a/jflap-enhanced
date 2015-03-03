package enhanced;

import java.util.*;

public class RegexLibrary
{
	public static HashMap<String, String> h = new HashMap<String, String>();
	
	static 
	{
		h.put("(1+01*0)(1+01*0)*","A DFA that contains even number of 0s.");
		h.put("1*01*0(1+01*0)*","A DFA that contains even number of 0s.");
		h.put("a(b+a)*","A 2 State DFA consisting of strings {a,b} that starts with a.");
		h.put("a(a+b)*","A 2 State DFA consisting of strings {a,b} that starts with a.");
		h.put("1(1+00*1)*", "A 3 state DFA of Binary Odd Numbers without leading zeros.");
		h.put("1(00*1+1)*", "A 3 state DFA of Binary Odd Numbers without leading zeros.");
		h.put("(0*11*0)*0*11*", "A 2 state DFA that accepts Binary Odd Numbers.");
		h.put("(0+11+10(1+00)*01)(11+10(1+00)*01)*", "A 3 state DFA of binary numbers that only accepts strings divisible by 3.");
		h.put("((1+01)*000*1)*(1+01)*000*", "A 3 state automaton of binary numbers that only accepts strings divisibe by 4.");
		h.put("((1+01)*000*1)*(01+1)*000*", "A 3 state automaton of binary numbers that only accepts strings divisibe by 4.");
		h.put("((01+1)*000*1)*(01+1)*000*", "A 3 state automaton of binary numbers that only accepts strings divisibe by 4.");
		h.put("((01+1)*000*1)*(1+01)*000*", "A 3 state automaton of binary numbers that only accepts strings divisibe by 4.");
		h.put("(0+11*0(11*0)*0)(0+11*0(11*0)*0)*", "A 3 State DFA of binary strings which accepts strings that are divisible by 4.");
		h.put("(1+011)(1+011)*", "A 3 State DFA of Binary Strings in which every 0 is followed by 11");
		h.put("(1+011)(011+1)*", "A 3 State DFA of Binary Strings in which every 0 is followed by 11");
		h.put("(011+1)(011+1)*", "A 3 State DFA of Binary Strings in which every 0 is followed by 11");
		h.put("(011+1)(1+011)*", "A 3 State DFA of Binary Strings in which every 0 is followed by 11");
		h.put("ab*a(a+bb*a)*","A 3 state DFA over strings {a,b} that starts and ends with an a.");
		h.put("(b+ab)*aa(b+a)*", "A 3 State DFA over {a,b} that must have atleast two consecutive 'a's");
		h.put("(b+ab)*aa(a+b)*", "A 3 State DFA over {a,b} that must have atleast two consecutive 'a's");
		h.put("(ab+b)*aa(a+b)*", "A 3 State DFA over {a,b} that must have atleast two consecutive 'a's");
		h.put("(ab+b)*aa(b+a)*", "A 3 State DFA over {a,b} that must have atleast two consecutive 'a's");
		h.put("(0+1(0+1))((0+1)(0+1))*","A 3 State DFA of Binary Strings that either starts with 0 and has odd length OR starts with 1 and has even length.");
		h.put("(0+1(0+1))((1+0)(0+1))*","A 3 State DFA of Binary Strings that either starts with 0 and has odd length OR starts with 1 and has even length.");
		h.put("(0+1(0+1))((1+0)(1+0))*","A 3 State DFA of Binary Strings that either starts with 0 and has odd length OR starts with 1 and has even length.");
		h.put("(0+1(0+1))((0+1)(1+0))*","A 3 State DFA of Binary Strings that either starts with 0 and has odd length OR starts with 1 and has even length.");
		h.put("b*ab*ab*", "A 3 state DFA over {a,b} that Contains Exactly 2 'a's.");
		h.put("(0+11*00)*11*01(0+1)*", "A 4 State DFA of Binary Strings to Search for keyword 101.");
		h.put("(1*00*1)*1*00*", "A 2 State DFA of Binary Strings consisting of Even numbers.");
		h.put("a*b(bb)*", "A 3 State DFA over the Strings {a,b} containing any number of 'a's followed by an odd number of 'b's.");
		h.put("a*(c+b)a*(b+c)a*(b+c)(a+c+b)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(b+c)a*(b+c)a*(b+c)(a+c+b)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(c+b)a*(c+b)a*(b+c)(a+c+b)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(b+c)a*(c+b)a*(b+c)(a+c+b)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(c+b)a*(b+c)a*(c+b)(a+c+b)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(b+c)a*(b+c)a*(c+b)(a+c+b)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(c+b)a*(c+b)a*(c+b)(a+c+b)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(b+c)a*(c+b)a*(c+b)(a+c+b)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(c+b)a*(b+c)a*(b+c)(a+b+c)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(b+c)a*(b+c)a*(b+c)(a+b+c)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(c+b)a*(c+b)a*(b+c)(a+b+c)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(b+c)a*(c+b)a*(b+c)(a+b+c)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(c+b)a*(b+c)a*(c+b)(a+b+c)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(b+c)a*(b+c)a*(c+b)(a+b+c)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(c+b)a*(c+b)a*(c+b)(a+b+c)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("a*(b+c)a*(c+b)a*(c+b)(a+b+c)*", "A 4 state Dfa over {a,b,c} which must have atleast 3 occurences of either b or c and can have any number of as.");
		h.put("(1*01*0)*1*01*", "A DFA that contains odd number of 0s");
		h.put("(0*10*1)*0*10*", "A DFA that contains odd number of 1s.");
		h.put("(0+10*1)(0+10*1)*", "A DFA that contains an even number of 1s.");
		h.put("(b*ab*a)*b*ab*", "A DFA that contains odd number of 'a's.");
		h.put("(a*ba*b)*a*ba*", "A DFA that contains odd number of 'b's");
		h.put("(a+ba*b)(a+ba*b)*", "A DFA that contains an even number of 'b's");
		h.put("(b+ab*a)(b+ab*a)*", "A DFA that contains even number of 'a's");
		h.put("b(b+a)*", "A 2 State DFA consisting of strings {a,b} that starts with b");
		h.put("b(a+b)*", "A 2 State DFA consisting of strings {a,b} that starts with b");
		h.put("0(1+0)*", "A 2 State DFA consisting of strings {0,1} that starts with 0");
		h.put("0(0+1)*", "A 2 State DFA consisting of strings {0,1} that starts with 0");
		h.put("1(1+0)*", "A 2 State DFA consisting of strings {0,1} that starts with 1");
		h.put("1(1+0)*", "A 2 State DFA consisting of strings {0,1} that starts with 1");
		h.put("a*ba*ba*","A 3 state DFA over {a,b} that Contains Exactly 2 'b's" );
		h.put("(aa*b+bb*a)(a+b)*","A 3 state DFA over {a,b} that Contains at least an 'a' and a 'b'" );
		h.put("(1+00*11)*00*10(1+0)*","A 4 State DFA of Binary Strings to Search for keyword 010" );
		h.put("(0+10)*111*0(1+0)*","A 4 State DFA of Binary Strings to Search for keyword 110" );
		h.put("(1+01)*000*1(0+1)*", "A 4 State DFA of Binary Strings to Search for keyword 001");
		h.put("1*00*1(00*1)*1(0+1)*","A 4 State DFA of Binary Strings to Search for keyword 011");
		h.put("0*11*0(11*0)*0(0+1)*","A 4 State DFA of Binary Strings to Search for keyword 100" );
		h.put("(0+10+110)*111(0+1)*","A 4 State DFA of Binary Strings to Search for keyword 111" );
		h.put("(1+01+001)*000(0+1)*"," A 4 State DFA of Binary Strings to Search for keyword 000" );
		h.put("(aa+bb+(ab+ba)(bb+aa)*(ba+ab))(aa+bb+(ab+ba)(bb+aa)*(ba+ab))*","A DFA over {a,b} containing even numbers of 'a's and 'b's" );
		h.put("((bb+ba(aa)*ab)*(a+ba(aa)*b)(b(aa)*b)*(a+b(aa)*ab))*(bb+ba(aa)*ab)*(a+ba(aa)*b)(b(aa)*b)*","A 4 State DFA over the strings {a,b} in which the number of 'a's is odd and the number of 'b's is even" );
		h.put("((aa+bb)*(ab+ba)(bb+aa)*(ba+ab))*(aa+bb)*(ab+ba)(bb+aa)*","A 4 State DFA over the strings {a,b} in which both the number of 'a's and 'b's are odd" );
		h.put("((aa+ab(bb)*ba)*(b+ab(bb)*a)(a(bb)*a)*(b+a(bb)*ba))*(aa+ab(bb)*ba)*(b+ab(bb)*a)(a(bb)*a)*","A 4 State DFA over the strings {a,b} in which the number of 'a's are even and the number of 'b's are odd");
		h.put("(00+11+(01+10)(11+00)*(10+01))(00+11+(01+10)(11+00)*(10+01))*","A 4 state DFA of Binary Strings in which there are even number of '0's and even number of '1's" );
		h.put("((00+11)*(01+10)(11+00)*(10+01))*(00+11)*(01+10)(11+00)*","A 4 state DFA of Binary Strings in which the number of '0's and '1's are both odd" );
		h.put("((00+01(11)*10)*(1+01(11)*0)(0(11)*0)*(1+0(11)*10))*(00+01(11)*10)*(1+01(11)*0)(0(11)*0)*","A 4 state DFA of Binary Strings in which the number of '0's is even and the number of '1's is odd" );
		h.put("((11+10(00)*01)*(0+10(00)*1)(1(00)*1)*(0+1(00)*01))*(11+10(00)*01)*(0+10(00)*1)(1(00)*1)*","A 4 state DFA of Binary Strings in which the number of '0's is odd and the number of '1's is even" );
		h.put("((0+10+110)*1111*0)*(0+10+110)*1111*","A 4 State DFA of binary strings that ends with 111" );
		h.put("(1+01+000*1(000*1)*01)*000*1(000*1)*1(1+0)*","A 5 State DFA of Binary Strings that contains two consecutve 0s followed by two consectuve 1s" );
		h.put("b*ab*ab*a(a+bb)*","A 5 State DFA over the strings {a,b} that contains atleast 3 'a's and ends with an even number of 'b's" );
		h.put("(0+101+11(01)*(1+00)1+(100+11(01)*(1+00)0)(1+0(01)*(1+00)0)*0(01)*(1+00)1)1*", "A 5 State DFA of binary strings which are divisible by 5");
		h.put("(ab+aa(ba)*bb+ba+bb(ab)*aa)(ab+aa(ba)*bb+ba+bb(ab)*aa)*","A 5 State DFA over the strings {a,b} having equal no of 'a's and 'b's but no trailing 'a's can exceed b by more than 2 and vice versa" );
		h.put("111(1+0)*","A 5 State DFA of binary strings that begin with 111" );
		h.put("(1+00*11+00*10(00*10)*00*11)*00*10(00*10)*1(1+0)*","A 5 State DFA of Binary Strings that contains 0101" );
		h.put("((0+10+111*00)*111*01(11*01)*(0+11*00))*(0+10+111*00)*111*01(11*01)*","A 5 State DFA of Binary Strings that contains 1101" );
		h.put("(0+11*0(11*0)*00)*11*0(11*0)*01(1+0)*","A 5 State DFA of binary strings that have the substring 1001" );
		h.put("1*01*01*01*01*","A 6 State DFA of Binary Strings that contains exactly four 0s." );
		h.put("(0+10)*11(0+1)0*1(00*1)*1(1+0)*","A 6 State DFA of Binary Strings with aleast two occurences of atleast two consecutive '1's with the two occurences not being adjacent" );
		h.put("(ab+aa(ba)*bb+aa(ba)*a(b(ba)*a)*b(ba)*bb+ba+bb(ab)*aa+bb(ab)*b(a(ab)*b)*a(ab)*aa)(ab+aa(ba)*bb+aa(ba)*a(b(ba)*a)*b(ba)*bb+ba+bb(ab)*aa+bb(ab)*b(a(ab)*b)*a(ab)*aa)*","A 7 state DFA of Binary Number containing  for equal numbers of a and b where the number of a's does not exceed the number of b's  by more than 3, at any instance, and vice versa" );
		h.put("0*11*0(11*0)*00*1(00*1)*1(00*1(00*1)*1)*1(1+00*1(00*1)*1(00*1(00*1)*1)*1)*","A 7 State DFA of binary strings which  contains substring 100 before substring 111" );
		h.put("(0+10)*111*00*1(00*1)*1(0+1)*","A DFA of Binary Strings with atleast two occurences of at least two consecutive 1s with the two occurences not being adjacent.");
		h.put("aa(aa)*bb(bb)*","A DFA of strings over {a,b} of the form a^2n b^2m where n and m are positive integers.");
		h.put("(b+a)(a+b)+(b+a)(a+b)(b+a)(b+a)","A DFA of Strings Over {a,b} whose length is divisible by 2 but not by 3.");
		h.put("00(00)*+(1+00(00)*1)(0(00)*1)*(?+0(00)*)+(01+00(00)*01+(1+00(00)*1)(0(00)*1)*(1+0(00)*01))(01+00(00)*01+(1+00(00)*1)(0(00)*1)*(1+0(00)*01))*(00(00)*+(1+00(00)*1)(0(00)*1)*(?+0(00)*))","A DFA of binary Strings such that if they are of even length, then the number they represent is also even and if the length is odd, then the number is also odd.");
		h.put("b*ab*a(b+ab*a)*cb*a(b+ab*a)*","A DFA of strings over {a,b,c} with a single c before which an even number of a's and any number of b's are present in any order. After the c, there must be an odd number of a's and any number of b's in any order.");
		h.put("((b+aa(aa)*b)c+(ab+aa(aa)*ab+(b+aa(aa)*b)b)(bb)*bc)c(cc)*","A DFa with strings of the form a^nb^mc^k where n+m is odd and  k is even with n,m,k>0.");
		h.put("(ab+aab+aaab+aaaab)bb*+(ab+aab+aaab+aaaab)bb*c+(ab+aab+aaab+aaaab)bb*cc","A DFa with strings of the form a^nb^mc^k where n<=4, m>=2,    k<=2 with n,m,k>0.");
		h.put("(0*11*0(11*0)*0)*0*11*0(11*0)*","A 3 DFA Binary Numbers that are twice an odd number (ends with 10)");
		h.put("(00+(1+01)(01)*(1+00))(0+1)*","A DFA of Binary Strings containing at least one 00 and atleast one 11.");
		h.put("(c+b+a)(c+b+a)(a+b+c)c(a+b+c)*","A DFA over strings {a,b,c} in which the fourth symbol from the beginning is a c.");
		h.put("((1+0)(0+1))*(1+0)","A DFA over strings {0,1} where total length of the string is odd.");
		h.put("((b+a)(a+b))*(b+a)","A DFA over {a,b} where total length of the string is odd.");
		h.put("(0+1)(1+0)((0+1)(1+0))*","A DFA over {0,1} where total length of the string is even");
		h.put("(a+b)(a+b)((a+b)(a+b))*","A DFA over {a,b} where total length of the string is even.");
		h.put("(1+0)(1+0)(0+1)((0+1)(1+0)(0+1))*","A DFA over {0,1} where the total length of the string is a multiple of 3.");
		h.put("(b+a)(b+a)(b+a)((a+b)(b+a)(b+a))*","A DFA over {a,b} where the total length of the string is a multiple of 3.");
		h.put("00(0+1)*","A DFA over {0,1} where all strings start with 00");
		h.put("11(0+1)*","A DFA Over {0,1} where all strings start with 11");
		h.put("aa(a+b)*","A DFA over {a,b} where all strings start with aa");
		h.put("bb(a+b)*","A DFA over {a,b} where all strings start with bb.");
		h.put("0(0+1)*","A DFA over {0,1} where all strings start with 0");
		h.put("1(0+1)*","A DFA over {0,1} where all strings start with 1");
		h.put("a(a+b)*","A DFA over {a,b} where all strings start with a.");
		h.put("b(a+b)*","A DFA over {a,b} where all strings start with b.");
		h.put("01(0+1)*","A DFA over {0,1} where all strings start with 01");
		h.put("10(0+1)*","A DFA over {0,1} where all strings start with 10");
		h.put("ab(a+b)*","A DFA over {a,b} where all strings start with ab");
		h.put("ba(a+b)*","A DFA over {a,b} where all strings start with ba");
		h.put("(1+011)(1+011)*","A DFA over Binary Strings in which every 0 is followed by 11");
		h.put("(1+01)(1+01)*","A DFA over Binary Strings with no consecutive zeros");
		h.put("ab(ab)*","A DFA over {a,b} of the form (ab)^n where n>0");
		h.put("(aa*b+ba(b+aa*b))((a+ba)(b+aa*b))*","A DFA over {a,b} where strings end with either ab or ba");
		h.put("(ab(a+b)bb*a+ab(a+b)aa*bb*a)(bb*a+aa*bb*a)*","A DFA over strings {a,b} of the form abwba where 'w' is any string over the same alphabet");
		h.put("1111*0(11*0)*0((1+01)1*0(11*0)*0)*","A DFA over binary strings that start with 111 and end with 100 with each string being atlest five characters long");
		h.put("0*11*0(11*0)*00*1(00*1)*1(00*1(00*1)*1)*1(00*1(00*1)*1(00*1(00*1)*1)*1+1)*","A 7 State DFA of binary strings which  contains substring 100 before substring 111" );
		h.put("(10+0)*111*00*1(00*1)*1(0+1)*","A DFA of Binary Strings with atleast two occurences of at least two consecutive 1s with the two occurences not being adjacent.");
		h.put("(10+0)*111*00*1(00*1)*1(1+0)*","A DFA of Binary Strings with atleast two occurences of at least two consecutive 1s with the two occurences not being adjacent.");
		h.put("(0+10)*111*00*1(00*1)*1(1+0)*","A DFA of Binary Strings with atleast two occurences of at least two consecutive 1s with the two occurences not being adjacent.");
		h.put("00(00)*+(00(00)*1+1)(0(00)*1)*(?+0(00)*)+(01+00(00)*01+(1+00(00)*1)(0(00)*1)*(1+0(00)*01))(01+00(00)*01+(1+00(00)*1)(0(00)*1)*(1+0(00)*01))*(00(00)*+(1+00(00)*1)(0(00)*1)*(?+0(00)*))","A DFA of binary Strings such that if they are of even length, then the number they represent is also even and if the length is odd, then the number is also odd.");
		h.put("b*ab*a(ab*a+b)*cb*a(b+ab*a)*","A DFA of strings over {a,b,c} with a single c before which an even number of a's and any number of b's are present in any order. After the c, there must be an odd number of a's and any number of b's in any order.");
		h.put("b*ab*a(ab*a+b)*cb*a(ab*a+b)*","A DFA of strings over {a,b,c} with a single c before which an even number of a's and any number of b's are present in any order. After the c, there must be an odd number of a's and any number of b's in any order.");
		h.put("b*ab*a(b+ab*a)*cb*a(ab*a+b)*","A DFA of strings over {a,b,c} with a single c before which an even number of a's and any number of b's are present in any order. After the c, there must be an odd number of a's and any number of b's in any order.");
		h.put("((aa(aa)*b+b)c+(ab+aa(aa)*ab+(b+aa(aa)*b)b)(bb)*bc)c(cc)*","A DFa with strings of the form a^nb^mc^k where n+m is odd and  k is even with n,m,k>0.");
		h.put("(00+(01+1)(01)*(1+00))(0+1)*","A DFA of Binary Strings containing at least one 00 and atleast one 11.");
		h.put("(00+(01+1)(01)*(00+1))(0+1)*","A DFA of Binary Strings containing at least one 00 and atleast one 11.");
		h.put("(00+(1+01)(01)*(00+1))(0+1)*","A DFA of Binary Strings containing at least one 00 and atleast one 11.");
		h.put("(c+b+a)(a+b+c)(a+b+c)c(a+b+c)*","A DFA over strings {a,b,c} in which the fourth symbol from the beginning is a c.");
		h.put("(a+b+c)(a+b+c)(a+b+c)c(a+b+c)*","A DFA over strings {a,b,c} in which the fourth symbol from the beginning is a c.");
		h.put("(a+b+c)(c+b+a)(a+b+c)c(a+b+c)*","A DFA over strings {a,b,c} in which the fourth symbol from the beginning is a c.");
		h.put("((0+1)(0+1))*(1+0)","A DFA over strings {0,1} where total length of the string is odd.");
		h.put("((0+1)(1+0))*(1+0)","A DFA over strings {0,1} where total length of the string is odd.");
		h.put("((1+0)(1+0))*(1+0)","A DFA over strings {0,1} where total length of the string is odd.");
		h.put("((a+b)(a+b))*(b+a)","A DFA over {a,b} where total length of the string is odd.");
		h.put("((b+a)(b+a))*(b+a)","A DFA over {a,b} where total length of the string is odd.");
		h.put("((a+b)(b+a))*(b+a)","A DFA over {a,b} where total length of the string is odd.");
		h.put("((b+a)(a+b))*(a+b)","A DFA over {a,b} where total length of the string is odd.");
		h.put("((a+b)(a+b))*(a+b)","A DFA over {a,b} where total length of the string is odd.");
		h.put("((b+a)(b+a))*(a+b)","A DFA over {a,b} where total length of the string is odd.");
		h.put("((a+b)(b+a))*(a+b)","A DFA over {a,b} where total length of the string is odd.");
		h.put("(0+1)(0+1)((0+1)(1+0))*","A DFA over {0,1} where total length of the string is even");
		h.put("(0+1)(0+1)((0+1)(0+1))*","A DFA over {0,1} where total length of the string is even");
		h.put("(0+1)(1+0)((0+1)(0+1))*","A DFA over {0,1} where total length of the string is even");
		h.put("00(1+0)*","A DFA over {0,1} where all strings start with 00");
		h.put("11(1+0)*","A DFA Over {0,1} where all strings start with 11");
		h.put("aa(b+a)*","A DFA over {a,b} where all strings start with aa");
		h.put("bb(b+a)*","A DFA over {a,b} where all strings start with bb.");
		h.put("0(1+0)*","A DFA over {0,1} where all strings start with 0");
		h.put("1(1+0)*","A DFA over {0,1} where all strings start with 1");
		h.put("a(b+a)*","A DFA over {a,b} where all strings start with a.");
		h.put("b(b+a)*","A DFA over {a,b} where all strings start with b.");
		h.put("01(1+0)*","A DFA over {0,1} where all strings start with 01");
		h.put("10(1+0)*","A DFA over {0,1} where all strings start with 10");
		h.put("ab(b+a)*","A DFA over {a,b} where all strings start with ab");
		h.put("ba(b+a)*","A DFA over {a,b} where all strings start with ba");
		h.put("(0+1)(1+0)((1+0)(1+0))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(1+0)(1+0)((1+0)(1+0))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(0+1)(0+1)((1+0)(1+0))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(1+0)(0+1)((1+0)(1+0))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(0+1)(1+0)((0+1)(1+0))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(1+0)(1+0)((0+1)(1+0))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(0+1)(0+1)((0+1)(1+0))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(1+0)(0+1)((0+1)(1+0))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(0+1)(1+0)((1+0)(0+1))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(1+0)(1+0)((1+0)(0+1))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(0+1)(0+1)((1+0)(0+1))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(1+0)(0+1)((1+0)(0+1))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(0+1)(1+0)((0+1)(0+1))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(1+0)(1+0)((0+1)(0+1))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(0+1)(0+1)((0+1)(0+1))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(1+0)(0+1)((0+1)(0+1))*", "A DFA over {0,1} which takes strings of even length.");
		h.put("(0+11+10(1+00)*01)(0+11+10(1+00)*01)*", "A DFA of binary numbers that only accepts strings divisible by 3.");
		h.put("(0+11*00)*11*01(1+0)*", "A DFA of Binary Strings to Search for keyword 101");
		h.put("(0+11*00)*11*01(0+1)*", "A DFA of Binary Strings to Search for keyword 101");
		h.put("(11*00+0)*11*01(1+0)*", "A DFA of Binary Strings to Search for keyword 101");
		h.put("(11*00+0)*11*01(0+1)*", "A DFA of Binary Strings to Search for keyword 101");
		h.put("(aa*b+bb*a)(b+a)*", "A DFA over {a,b} that Contains at least an 'a' and a 'b'");
		h.put("(aa*b+bb*a)(a+b)*", "A DFA over {a,b} that Contains at least an 'a' and a 'b'");
		h.put("(bb*a+aa*b)(a+b)*", "A DFA over {a,b} that Contains at least an 'a' and a 'b'");
		h.put("(bb*a+aa*b)(b+a)*", "A DFA over {a,b} that Contains at least an 'a' and a 'b'");
		h.put("(1+01)*000*1(1+0)*", "A DFA of Binary Strings to Search for keyword 001.");
		h.put("(01+1)*000*1(1+0)*", "A DFA of Binary Strings to Search for keyword 001.");
		h.put("(01+1)*000*1(0+1)*", "A DFA of Binary Strings to Search for keyword 001.");
		h.put("(1+01)*000*1(0+1)*", "A DFA of Binary Strings to Search for keyword 001.");
		h.put("1*00*1(00*1)*1(1+0)*", "A DFA of Binary Strings to Search for keyword 011.");
		h.put("1*00*1(00*1)*1(0+1)*", "A DFA of Binary Strings to Search for keyword 011.");
		h.put("0*11*0(11*0)*0(1+0)*", "A DFA of Binary Strings to Search for keyword 100.");
		h.put("0*11*0(11*0)*0(0+1)*", "A DFA of Binary Strings to Search for keyword 100.");
		h.put("(0+10+110)*111(1+0)*", "A DFA of Binary Strings to Search for keyword 111.");
		h.put("(0+10+110)*111(0+1)*", "A DFA of Binary Strings to Search for keyword 111.");
		h.put("(10+0+110)*111(0+1)*", "A DFA of Binary Strings to Search for keyword 111.");
		h.put("(10+0+110)*111(1+0)*", "A DFA of Binary Strings to Search for keyword 111.");
		h.put("(1+01+001)*000(1+0)*", "A DFA of Binary Strings to Search for keyword 000.");
		h.put("(01+1+001)*000(1+0)*", "A DFA of Binary Strings to Search for keyword 000.");
		h.put("(01+1+001)*000(0+1)*", "A DFA of Binary Strings to Search for keyword 000.");
		h.put("(1+01+001)*000(0+1)*", "A DFA of Binary Strings to Search for keyword 000.");
		h.put("(00+(1+01)(01)*(1+00))(1+0)*", "A DFA of Binary Strings containing at least one 00 and atleast one 11.");
		h.put("(00+(01+1)(01)*(1+00))(1+0)*", "A DFA of Binary Strings containing at least one 00 and atleast one 11.");
		h.put("(00+(1+01)(01)*(00+1))(1+0)*", "A DFA of Binary Strings containing at least one 00 and atleast one 11.");
		h.put("(00+(01+1)(01)*(00+1))(1+0)*", "A DFA of Binary Strings containing at least one 00 and atleast one 11.");
		h.put("(00+(1+01)(01)*(1+00))(0+1)*", "A DFA of Binary Strings containing at least one 00 and atleast one 11.");
		h.put("(00+(01+1)(01)*(1+00))(0+1)*", "A DFA of Binary Strings containing at least one 00 and atleast one 11.");
		h.put("(00+(1+01)(01)*(00+1))(0+1)*", "A DFA of Binary Strings containing at least one 00 and atleast one 11.");
		h.put("(00+(01+1)(01)*(00+1))(0+1)*", "A DFA of Binary Strings containing at least one 00 and atleast one 11.");
		h.put("(a+c+b)(c+b+a)(a+c+b)c(b+c+a)*", "A DFA over strings {a,b,c} in which the fourth symbol from the beginning is a c.");
		h.put("(a+b+c)(c+b+a)(a+c+b)c(b+c+a)*", "A DFA over strings {a,b,c} in which the fourth symbol from the beginning is a c.");
		h.put("(b+c+a)(c+b+a)(a+c+b)c(b+c+a)*", "A DFA over strings {a,b,c} in which the fourth symbol from the beginning is a c.");
		h.put("(b+a+c)(c+b+a)(a+c+b)c(b+c+a)*", "A DFA over strings {a,b,c} in which the fourth symbol from the beginning is a c.");
		h.put("(c+a+b)(c+b+a)(a+c+b)c(b+c+a)*", "A DFA over strings {a,b,c} in which the fourth symbol from the beginning is a c.");
		h.put("(c+b+a)(c+b+a)(a+c+b)c(b+c+a)*", "A DFA over strings {a,b,c} in which the fourth symbol from the beginning is a c.");
		h.put("(a+b)(b+a)(b+a)((b+a)(b+a)(b+a))*", "A DFA over {a,b} where the total length of the string is a multiple of 3.");
		h.put("(a+b)(b+a)(a+b)", "A DFA over {a,b} of binary strings that should be of length 3.");
		h.put("(b+a)(b+a)(a+b)", "A DFA over {a,b} of binary strings that should be of length 3.");
		h.put("(a+b)(a+b)(a+b)", "A DFA over {a,b} of binary strings that should be of length 3.");
		h.put("(b+a)(a+b)(a+b)", "A DFA over {a,b} of binary strings that should be of length 3.");
		h.put("(a+b)(b+a)(b+a)", "A DFA over {a,b} of binary strings that should be of length 3.");
		h.put("(b+a)(b+a)(b+a)", "A DFA over {a,b} of binary strings that should be of length 3.");
		h.put("(a+b)(a+b)(b+a)", "A DFA over {a,b} of binary strings that should be of length 3.");
		h.put("(b+a)(a+b)(b+a)", "A DFA over {a,b} of binary strings that should be of length 3.");
		h.put("b*ab*a(a+b(b+a))*", "A DFA for strings over {a,b} containing atleast two 'a's and ending with an even no of 'b's.");
		h.put("b*ab*a(a+b(a+b))*", "A DFA for strings over {a,b} containing atleast two 'a's and ending with an even no of 'b's.");
		h.put("ab+aabb+aaabbb+aaaabbbb+aaaaabbbbb", "A DFA over the strings {a,b} for the language a^nb^n with n less than 6.");
		h.put("aaaa+abba+acca+baab+bbbb+bccb+caac+cbbc+cccc", "A DFA over {a,b} which accepts Palindromes of length 4.");
	}
}