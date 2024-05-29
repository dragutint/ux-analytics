import pandas as pd
from scipy.stats import ttest_ind
import matplotlib.pyplot as plt
import seaborn as sns

# Load the data
data = pd.read_csv("../../resources/characteristic-vectors-2605.csv")

# Separate data for each form
form1_data = data[data['form_number'] == 1]
form2_data = data[data['form_number'] == 2]

# Calculate mean and standard deviation for key metrics
metrics = [
    'formCompletionTime',
    'idleTime',
    'totalClicks',
    'totalKeyboardPresses',
    'totalMouseLength',
    'totalScrollLength',
    'correctionRate'
]

for metric in metrics:
    print(f"Form 1 {metric} - Mean: {form1_data[form1_data['name'] == metric]['value'].mean()}, Std Dev: {form1_data[form1_data['name'] == metric]['value'].std()}")
    print(f"Form 2 {metric} - Mean: {form2_data[form2_data['name'] == metric]['value'].mean()}, Std Dev: {form2_data[form2_data['name'] == metric]['value'].std()}")

# Perform t-tests for each metric
results = {}
for metric in metrics:
    form1_metric_values = form1_data[form1_data['name'] == metric]['value']
    form2_metric_values = form2_data[form2_data['name'] == metric]['value']

    t_stat, p_value = ttest_ind(form1_metric_values, form2_metric_values, equal_var=False)
    results[metric] = (t_stat, p_value)
    print(f"T-test for {metric}: t-statistic = {t_stat}, p-value = {p_value}")

# Display the results
for metric, result in results.items():
    print(f"Metric: {metric}, T-statistic: {result[0]}, P-value: {result[1]}")

# Set the style for the plots
sns.set(style="whitegrid")

# Create box plots for each metric
fig, axs = plt.subplots(4, 2, figsize=(15, 10))
fig.suptitle('Comparison of Form Metrics')

for i, metric in enumerate(metrics):
    row = i // 2
    col = i % 2
    sns.boxplot(x='form_number', y='value', data=data[data['name'] == metric], ax=axs[row, col])
    axs[row, col].set_title(metric)
    axs[row, col].set_xlabel('Form Number')
    axs[row, col].set_ylabel('Value')

plt.tight_layout(rect=[0, 0, 1, 0.96])
plt.show()