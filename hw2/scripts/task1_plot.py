import matplotlib.pyplot as plt
import numpy as np

# Data for the plot
threads = [2, 4, 8, 16, 20, 32]
bakery_avg_times = [4.8, 4.6, 7.8, 10.0, 23.4, 64.8]
filter_avg_times = [4.0, 10.2, 21.4, 46.2, 56.4, 154.0]
# Creating a bar plot with log scale for both Bakery and Filter lock times
fig, ax = plt.subplots()

bar_width = 0.35
index = np.arange(len(threads))

# Bar positions for Bakery and Filter lock times with log scale
bar1 = ax.bar(index, bakery_avg_times, bar_width, label='Bakery Lock')
bar2 = ax.bar(index + bar_width, filter_avg_times, bar_width, label='Filter Lock')

# Labels and titles
ax.set_xlabel('Thread Count')
ax.set_ylabel('Average Time (ms) (Log Scale)')
#ax.set_title('Log-Scaled Average Time vs Thread Count for Bakery and Filter Locks (Bar Graph)')
ax.set_xticks(index + bar_width / 2)
ax.set_xticklabels(threads)
ax.legend()
ax.yaxis.grid(True)
# Save the log-scaled bar graph as a PDF
plt.savefig('../outputs/log_scaled_bar_graph.png')

