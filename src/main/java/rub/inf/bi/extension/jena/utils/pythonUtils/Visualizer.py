import time
import numpy as np
import matplotlib.pyplot as plt

def vis_time_compl():
    N = 8
    y_BSP = [2782, 3427, 5394, 10427, 28392, 161855, 1312450, 13824101]
    y_CGO = [4350, 5147, 6055, 9751, 13780, 30507, 242751, 3521444]

    # the x locations for the bars
    ind = np.arange(N)  

    # the width of the bars
    width = 0.35       

    plt.semilogy(range(1, N+1), y_BSP)
    plt.semilogy(range(1, N+1), y_CGO)

    # display plot legend
    plt.legend(('BSP', 'CSO'))

    # # add some text for labels, title and axes ticks
    plt.ylabel('Time (ms)')
    plt.title('Time complexity of BSP and CGO')
    plt.xticks(ind + width, ('Level 1', 'Level 2', 'Level 3', 'Level 4', 'Level 5', 'Level 6', 'Level 7', 'Level 8'))

    # display the plot
    plt.show()

def vis_query_res():
    x_level = range(1, 9)

    y_BSP = [0.6, 1.0, 0.941, 0.982, 0.995, 1.0, 0.997, 0.996]
    y_CGO = [1.0, 0.984, 0.941, 0.893, 0.843, 0.814, 0.822, 0.871]

    # Plot the time complexity data
    plt.plot(x_level, y_BSP, 'go-')
    plt.plot(x_level, y_CGO, 'yo-')
    plt.legend(('BSP', 'CSO'))
    plt.xlabel('Level (k)')
    plt.ylabel('Accuracy (rounding 3 digits after comma)')
    plt.title('Correctness of query result')
    plt.show()

def vis_pie_chart():

    # Example data
    labels = ['Label 1', 'Label 2', 'Label 3']
    sizes = [30, 20, 50]

    # Create pie chart
    fig1, ax1 = plt.subplots()
    ax1.pie(sizes, labels=labels, autopct='%1.1f%%', startangle=90)

    # Set aspect ratio to be equal so that pie is drawn as a circle
    ax1.axis('equal')

    # Show plot
    plt.show()

vis_query_res()