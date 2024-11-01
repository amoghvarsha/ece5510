import matplotlib.pyplot as plt
import numpy as np

# Data
thread_counts = [2, 4, 8, 16, 32, 64]
local_averages = [15, 22, 22.6, 21.8, 19, 8.2]  # Local averages

# Average of averages for remote server
remote_avg_averages = [
    np.mean([30.2, 30.4, 38, 26.4, 27.6]),  # 2 threads
    np.mean([74, 84.6, 72.8, 74.6, 102.6]),  # 4 threads
    np.mean([104, 102.2, 105.2, 95.8, 100.2]),  # 8 threads
    np.mean([104.8, 106.4, 103.2, 108.8, 106.8]),  # 16 threads
    np.mean([120, 111.4, 108.4, 121.6, 110.4]),  # 32 threads
    np.mean([83.2, 87.4, 85.6, 82.8, 85.4])  # 64 threads
]

# Plotting the bar graph
width = 0.35  # Width of the bars
x = np.arange(len(thread_counts))  # The label locations

fig, ax = plt.subplots()
rects1 = ax.bar(x - width/2, local_averages, width, label='Local', color='blue')
rects2 = ax.bar(x + width/2, remote_avg_averages, width, label='Remote', color='orange')

# Add labels and title
ax.set_ylabel('Average Time (ms)')
ax.set_xlabel('Thread Count')
#ax.set_title('Average runtime vs thread count for local and remote server execution (Average of Averages)')
ax.set_xticks(x)
ax.set_xticklabels(thread_counts)
ax.legend()

ax.yaxis.grid(True)

# Save the plot
plt.savefig('../outputs/average_runtime_plot_for_latex.png')
