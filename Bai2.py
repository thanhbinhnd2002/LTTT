from collections import Counter
import numpy as np
import math

print("Huffman, Shannon-Fano Compression Program")
print("=================================================================")
import collections
h = int(float(input("Enter 1 if you want to enter in command window, 2 if you are using input as file :")))
if h == 1:
    message = input("Enter the string you want to compress:")
elif h == 2:
    file = input("Enter the filename:")
    with open(file, 'r') as f:
        message = f.read()
else:
    print("You entered invalid input")
print("Entered string is:",message)                      #taking input from user

#Huffman part

# A Huffman Tree Node
class Node:
    def __init__(self, prob, symbol, left=None, right=None):
        # probability of symbol
        self.prob = prob
        # symbol 
        self.symbol = symbol
        # left node
        self.left = left
        # right node
        self.right = right
        # tree direction (0/1)
        self.code = ''

""" A helper function to print the codes of symbols by traveling Huffman Tree"""
codes = dict()

def Calculate_Codes(node, val=''):
    # huffman code for current node
    newVal = val + str(node.code)

    if(node.left):
        Calculate_Codes(node.left, newVal)
    if(node.right):
        Calculate_Codes(node.right, newVal)

    if(not node.left and not node.right):
        codes[node.symbol] = newVal
         
    return codes        

""" A helper function to calculate the probabilities of symbols in given message"""
def Calculate_Probability(message):
    symbols = dict()
    for element in message:
        if symbols.get(element) == None:
            symbols[element] = 1
        else: 
            symbols[element] += 1     
    return symbols

""" A helper function to obtain the encoded output"""
def Output_Encoded(message, coding):
    encoding_output = []
    for c in message:
      #  print(coding[c], end = '')
        encoding_output.append(coding[c])
        
    string = ''.join([str(item) for item in encoding_output])    
    return string
        
""" A helper function to calculate the space difference between compressed and non compressed message"""    
def Total_Gain(message, coding):
    before_compression = len(message) * 8 # total bit space to stor the message before compression
    after_compression = 0
    symbols = coding.keys()
    for symbol in symbols:
        count = message.count(symbol)
        after_compression += count * len(coding[symbol]) #calculate how many bit is required for that symbol in total
    print("Space usage before compression in Huffman encoding (in bits):", before_compression)    
    print("Space usage after compression in Huffman encoding (in bits):",  after_compression)           

def Huffman_Encoding(message):
    symbol_with_probs = Calculate_Probability(message)
    symbols = symbol_with_probs.keys()
    probabilities = symbol_with_probs.values()
    list1 = dict(collections.Counter(message))
    for key, value in list1.items():
        print(key,':' ,value)
    
    print("symbols: ", symbols)
    print("probabilities: ", probabilities)

    nodes = []
    
    # converting symbols and probabilities into huffman tree nodes
    for symbol in symbols:
        nodes.append(Node(symbol_with_probs.get(symbol), symbol))
    
    while len(nodes) > 1:
        # sort all the nodes in ascending order based on their probability
        nodes = sorted(nodes, key=lambda x: x.prob)
        # for node in nodes:  
        #      print(node.symbol, node.prob)
    
        # pick 2 smallest nodes
        right = nodes[0]
        left = nodes[1]
    
        left.code = 0
        right.code = 1
    
        # combine the 2 smallest nodes to create new node
        newNode = Node(left.prob+right.prob, left.symbol+right.symbol, left, right)
    
        nodes.remove(left)
        nodes.remove(right)
        nodes.append(newNode)
            
    huffman_encoding = Calculate_Codes(nodes[0])
    print("symbols with codes" , huffman_encoding)
    for key, value in huffman_encoding.items():
        print(key,':' ,value)
    Total_Gain(message, huffman_encoding)
    encoded_output = Output_Encoded(message,huffman_encoding)
    return encoded_output, nodes[0]  
    
 
def Huffman_Decoding(encoded_message, huffman_tree):
    tree_head = huffman_tree
    decoded_output = []
    for x in encoded_message:
        if x == '1':
            huffman_tree = huffman_tree.right   
        elif x == '0':
            huffman_tree = huffman_tree.left
        try:
            if huffman_tree.left.symbol == None and huffman_tree.right.symbol == None:
                pass
        except AttributeError:
            decoded_output.append(huffman_tree.symbol)
            huffman_tree = tree_head
        
    string = ''.join([str(item) for item in decoded_output])
    return string        

huffman_encoding, tree = Huffman_Encoding(message)

print("Encoded output in Huffman encoding: ", huffman_encoding)
print("Decoded Output in Huffman decoding: ", Huffman_Decoding(huffman_encoding,tree))

print("=================================================================")
# Path: Shannon-Fano.py

c = {}
def create_list(message):
    list = dict(collections.Counter(message))
    for key, value in list.items():
        print(key, ' : ', value)                         #creating the sorted list according to the probablity
    list_sorted = sorted(iter(list.items()), key = lambda k_v:(k_v[1],k_v[0]),reverse=True)
    final_list = []
    for key,value in list_sorted:
        final_list.append([key,value,''])
    return final_list

print("Shannon tree with merged pathways:")
def divide_list(list):
    if len(list) == 2:
        print([list[0]],[list[1]])               #printing merged pathways
        return [list[0]],[list[1]]
    else:
        n = 0
        for i in list:
            n+= i[1]
        x = 0
        distance = abs(2*x - n)
        j = 0
        for i in range(len(list)):               #shannon tree structure
            x += list[i][1]
            if distance < abs(2*x - n):
                j = i
    print(list[0:j+1], list[j+1:])               #printing merged pathways
    return list[0:j+1], list[j+1:]


def label_list(list):
    list1,list2 = divide_list(list)
    for i in list1:
        i[2] += '0'
        c[i[0]] = i[2]
    for i in list2:
        i[2] += '1'
        c[i[0]] = i[2]
    if len(list1)==1 and len(list2)==1:        #assigning values to the tree
        return
    label_list(list2)
    return c

code = label_list(create_list(message))
print("Shannon's Encoded Code:")
output = open("compressed.txt","w+")          # generating output binary
letter_binary = []
for key, value in code.items():
    print(key, ' : ', value)
    letter_binary.append([key,value])
print("Compressed file generated as compressed.txt")

for a in message:
    for key, value in code.items():
        if key in a:
            print(key, ' : ', value)
            output.write(value)
output = open("compressed.txt","r")
intermediate = output.readlines()
bitstring = ""
for digit in intermediate:
    bitstring = bitstring + digit
uncompressed_string =""
code =""
for digit in bitstring:
    code = code+digit
    pos=0
    for letter in letter_binary:               # decoding the binary and genrating original message
        if code ==letter[1]:
            uncompressed_string=uncompressed_string+letter_binary[pos] [0]
            code=""
        pos+=1

print("Encoded output in Shannon-Fano encoding: ", bitstring)
print("Your UNCOMPRESSED message is:")
print(uncompressed_string)

print("Độ dài văn bản là: ",len(message))
print("Độ dài chuỗi mã hóa Huffman: ", len(huffman_encoding))
print("Độ dài chuỗi mã hóa Shannon-Fano: ", len(bitstring), "\n")

def encoded_efficiency(message, encoded_message):
    total_char = len(message)
    total_bits = len(encoded_message)
    everage_code_length = total_bits / total_char
    freqs = Counter(message)
    P = [freq / total_char for freq in freqs.values()]
    H = -np.sum(P * np.log2(P))
    return H / everage_code_length

def redundancy(message, encoded_message):
    return 1 - encoded_efficiency(message, encoded_message)

print("Huffman encoded efficiency: ", encoded_efficiency(message, huffman_encoding))
print("Huffman encoded redundancy: ", redundancy(message, huffman_encoding), "\n")
print("Shannon encoded efficiency: ", encoded_efficiency(message, bitstring))
print("Shannon encoded redundancy: ", redundancy(message, bitstring))