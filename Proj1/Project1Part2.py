from SearchEnum import SearchEnum
import Graph
import sys
from collections import OrderedDict

# Template for Project 1 of CS 4341 - A2020
def Make_Queue_Node(state):
    node = []
    node.append(state)
    return node

def Make_Queue(node):
    queue = []
    queue.append(node)
    return queue
    
def Remove_Front(queue):
    return queue.pop(0)
def Terminal_State(node):
    return node[0]

def Expand(graph, node, visited):
    edges = []
    state = node[0]
    #print("Visited: ", visited)
    #print("Edge: ",state.edges)
    #print("state: ", state.name, "Edge: ",state.edges)
    for edge in state.edges:
        if edge not in visited:            
            edges.append(graph.getState(edge))
    visited.append(state.name)
      
    return edges, visited


def get_dist(node):
    dist = 0.0
    
    for i in range(len(node)-1):
        name = node[i+1].name
        d = node[i].edges[name]
        dist += d
    return dist
        
def get_h(node):
    return node[0].heuristic

def get_A_star(node):
    return get_dist(node) + get_h(node)
        
    
def Print_Queue(queue, search):
    prt = "["
    for node in queue:
        if search == SearchEnum.UNIFORM_COST_SEARCH:
            prt+= str(get_dist(node))
        elif search == SearchEnum.GREEDY_SEARCH or search == SearchEnum.HILL_CLIMBING or search == SearchEnum.BEAM_SEARCH:
            prt+= str(get_h(node))
        elif search == SearchEnum.A_STAR:
            prt+= str(get_A_star(node))
            
        prt += "<"
        for state in node:
            prt+= state.name
            prt += " "
            
        prt += "> "
    prt += "]"
    return prt

def print_result(expand, queue, isInformed):
    print("Expanded\tQueue")
    print(expand.name,"\t",Print_Queue(queue, isInformed))
        
def General_Search(problem, searchMethod):
    """
    Return the solution path or failure to reach state G from state S. 
    
    Parameters
    ----------
    problem : Graph.Graph
        The graph to search from S to G.
    searchMethod : SearchEnum
        The search method to use to search the graph.
    """
    initialState = 'S' # name of the initial state
    finalState = 'G' # Name of the final state

    depth_limit = 0
    # Implementation of the below pseudocode may vary slightly depending on the data structures used.
    loop = True
    limit = False
    while loop:
        if searchMethod == SearchEnum.ITERATIVE_DEEPENING_SEARCH:
            limit = True
            print("L = ", depth_limit)
        elif searchMethod == SearchEnum.DEPTH_LIMITED_SEARCH:
            limit = True
            depth_limit = 2
            loop = False
        else:          
            loop = False
        
        queue = Make_Queue(Make_Queue_Node(problem.getState(initialState)))
        visited = []
        #visited.append(initialState)
        while len(queue) > 0:
            print_result(queue[0][0], queue, searchMethod)
            node = Remove_Front(queue)
            if Terminal_State(node).name == finalState:
                    return node
            if not (limit and len(node) == depth_limit+1):
                opened_nodes, visited = Expand(problem, node, visited)
                queue, visited = expand_queue(node, queue,opened_nodes,problem,searchMethod, visited)
                
        depth_limit += 1
        

    return False
    
def get_order(nodesToAddToQueue):
    def sortLetter(state):
        return state.name
    
    nodesToAddToQueue.sort(key=sortLetter, reverse=True)
  
    return nodesToAddToQueue

def informed_search(expandedN, queue, nodesToAddToQueue, problem, search, visited):
    nodesToAddToQueue = get_order(nodesToAddToQueue)
    for state in nodesToAddToQueue:
        newNode = []
        newNode.append(state)
        notAdd = False
        for expanded in expandedN:
            if expanded in newNode:
                notAdd = True
            newNode.append(expanded)
        if not notAdd:
            queue.append(newNode)
    return queue

def depth_limit_search(expandedN, queue, nodesToAddToQueue, problem, search, visited):
    nodesToAddToQueue = get_order(nodesToAddToQueue)
    for state in nodesToAddToQueue:
        newNode = []
        newNode.append(state)
        notAdd = False
        for expanded in expandedN:
            if expanded in newNode:
                notAdd = True
            newNode.append(expanded)
        if not notAdd:
            queue.insert(0,newNode)
    return queue     
    

def beam_remove(queue):
    highest_path = len(queue[-1])
    same_level = []
    for node in queue:
        if len(node) == highest_path:
            same_level.append(node)
    if len(same_level) > 2 and len(same_level) == len(queue):        
        new_q = sort_informed(same_level,get_h)
        #print(Print_Queue(new_q, SearchEnum.BEAM_SEARCH))
        for i in range(2, len(new_q)):
            #print(new_q[i][0].name)
            queue.remove(new_q[i])  
    
    return queue
   
def remove_expensive(queue, func):
    to_remove = []
    for i in range(len(queue)-1):
        for j in range(i+1, len(queue)):
            if len(queue) > 1 and queue[i][0] == queue[j][0]:
                if func(queue[i])>func(queue[j]):
                    to_remove.append(queue[i])
                elif func(queue[i])<func(queue[j]):
                    to_remove.append(queue[j])
    return to_remove
    

def expand_queue(expandedN, queue, nodesToAddToQueue, problem, search, visited):
    """
    Add the new nodes created from the opened nodes to the queue based on the search strategy.

    Parameters
    ----------
    queue 
        The queue containing the possible nodes to expand upon for the search.
    newNodesToAddToQueue : list
        The list of nodes to add to the queue.
    problem : Graph.Graph
        The graph to search from S to G.
    searchMethod : SearchEnum
        The search method to use to search the graph.
    """
    #Fill in the below if and elif bodies to implement how the respective searches add new nodes to the queue.
    if search == SearchEnum.DEPTH_FIRST_SEARCH:
        nodesToAddToQueue = get_order(nodesToAddToQueue)
        for state in nodesToAddToQueue:
            newNode = []
            newNode.append(state)
            for expanded in expandedN:
                newNode.append(expanded)
            queue.insert(0,newNode)
            
        return queue,visited
    
    elif search == SearchEnum.BREADTH_FIRST_SEARCH:
        for state in nodesToAddToQueue:
            newNode = []            
            newNode.append(state)
            notAdd = False
            for expanded in expandedN:
                if expanded in newNode:
                    notAdd = True
                newNode.append(expanded)
            if not notAdd:
                queue.append(newNode)
        return queue,visited
            

    elif search == SearchEnum.DEPTH_LIMITED_SEARCH:
        queue = depth_limit_search(expandedN, queue, nodesToAddToQueue, problem, search, visited)
        return queue,visited
        

    elif search == SearchEnum.ITERATIVE_DEEPENING_SEARCH:
        queue = depth_limit_search(expandedN, queue, nodesToAddToQueue, problem, search, visited)
        return queue,visited

    elif search == SearchEnum.UNIFORM_COST_SEARCH:
        queue = informed_search(expandedN, queue, nodesToAddToQueue, problem, search, visited)
        queue = sort_informed(queue, get_dist)
        #print(len(queue))
        to_remove = remove_expensive(queue, get_dist)
        for remove in to_remove:
            queue.remove(remove)
        return queue,visited
        

    elif search == SearchEnum.GREEDY_SEARCH:
        queue = informed_search(expandedN, queue, nodesToAddToQueue, problem, search, visited)
        queue = sort_informed(queue,get_h)
        to_remove = remove_expensive(queue, get_h)
        for remove in to_remove:
            queue.remove(remove)
        return queue,visited
        

    elif search == SearchEnum.A_STAR:
        queue = informed_search(expandedN, queue, nodesToAddToQueue, problem, search, visited)
        queue = sort_informed(queue,get_A_star)
        #print(len(queue))
        to_remove = remove_expensive(queue, get_A_star)
        for remove in to_remove:
            queue.remove(remove)
        return queue,visited
        

    elif search == SearchEnum.HILL_CLIMBING:
        queue = informed_search(expandedN, queue, nodesToAddToQueue, problem, search, visited)
        queue = sort_informed(queue,get_h)
        new_q = []
        if len(queue) == 0:
            return queue, visited
        new_q.append(queue[0])
        return new_q, visited

    elif search == SearchEnum.BEAM_SEARCH:
        for state in nodesToAddToQueue:
            newNode = []
            newNode.append(state)
            for expanded in expandedN:
                newNode.append(expanded)
            queue.append(newNode)
            visited.append(expandedN[0].name)
        queue = beam_remove(queue)
        return queue,visited
        
    return


def sort_informed(nodeList, function):
    def sortV(node):
        return function(node), node[0].name,len(node),node[1].name
    #print(Print_Queue(nodeList, True))
    nodeList.sort(key=sortV, reverse=False)
    #print(Print_Queue(nodeList, True))
    return nodeList
    
def print_solution(node):
    solution = "Solution found: "
    for state in reversed(node):
        solution += state.name
        solution += " "
    return solution
    
def main(filename):
    """
    Entry point for this program. Parses the input and then runs each search on the parsed graph.

    Parameters
    ----------
    filename : str
        The name of the file with graph input to search
    """ 

    graph = readInput(filename)
    for search in SearchEnum:
        print(search.value)
        result = General_Search(graph, search)
        if (not result):
            print("failure to find path between S and G")
        else:
            print("\tgoal reached!")
            print(print_solution(result))
            # Print solution path here
        print()
def readInput(filename):
    """
    Build the graph from the given input file.

    Parameters
    ----------
    filename : str
        The name of the file with input to parse into a graph.
    """

    parsedGraph = Graph.Graph()
    isHeuristicSection = False # True if processing the heuristic values for the graph. False otherwise.
    sectionDivider = "#####"
    minCharsInLine = 3 # Each line with data must have at least 3 characters
    with open(filename, 'r') as input:
        for line in input.readlines():
            if (len(line) > minCharsInLine):
                line = line.strip()
                if(sectionDivider in line):
                    isHeuristicSection = True
                elif(isHeuristicSection):
                    state, heurStr = line.split(' ')
                    heuristic = float(heurStr)
                    parsedGraph.setHeuristic(state, heuristic)
                else:
                    state1, state2, costStr = line.split(' ')
                    cost = float(costStr)
                    parsedGraph.addStatesAndEdge(state1,state2, cost)
    for state_key in parsedGraph.states:
        state = parsedGraph.states[state_key]
        state.edges = OrderedDict(sorted(state.edges.items()))
    return parsedGraph   
if __name__ == "__main__": 
    if len(sys.argv) == 2:
        main(sys.argv[1])
    else:
        print("Must input the filename with the graph input to search.")