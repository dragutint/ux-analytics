import pandas as pd
from scipy.stats import ttest_ind
import matplotlib.pyplot as plt
import seaborn as sns

# Load the data
data = pd.read_csv("../../resources/characteristic-vectors-2605.csv")

# Display the first few rows of the data
print(data.head())

# Get all unique characteristics
characteristics = data['name'].unique()
print(characteristics)

# Separate data for each form
form1_data = data[data['form_number'] == 1]
form2_data = data[data['form_number'] == 2]

# Calculate mean and standard deviation for each characteristic
for characteristic in characteristics:
    form1_values = form1_data[form1_data['name'] == characteristic]['value']
    form2_values = form2_data[form2_data['name'] == characteristic]['value']

    print(f"Form 1 {characteristic} - Mean: {form1_values.mean()}, Std Dev: {form1_values.std()}")
    print(f"Form 2 {characteristic} - Mean: {form2_values.mean()}, Std Dev: {form2_values.std()}")

# Perform t-tests for each characteristic
results = {}
for characteristic in characteristics:
    form1_values = form1_data[form1_data['name'] == characteristic]['value']
    form2_values = form2_data[form2_data['name'] == characteristic]['value']

    t_stat, p_value = ttest_ind(form1_values, form2_values, equal_var=False)
    results[characteristic] = (t_stat, p_value)
    print(f"T-test for {characteristic}: t-statistic = {t_stat}, p-value = {p_value}")

# Display the results
for characteristic, result in results.items():
    print(f"Characteristic: {characteristic}, T-statistic: {result[0]}, P-value: {result[1]}")

# Set the style for the plots
sns.set(style="whitegrid")

# Determine number of rows and columns for subplots
num_characteristics = len(characteristics)
num_cols = 2
num_rows = (num_characteristics + 1) // num_cols

# Create box plots for each characteristic
fig, axs = plt.subplots(num_rows, num_cols, figsize=(15, num_rows * 5))
fig.suptitle('Comparison of Form Characteristics')

for i, characteristic in enumerate(characteristics):
    row = i // num_cols
    col = i % num_cols
    sns.boxplot(x='form_number', y='value', data=data[data['name'] == characteristic], ax=axs[row, col])
    axs[row, col].set_title(characteristic)
    axs[row, col].set_xlabel('Form Number')
    axs[row, col].set_ylabel('Value')

plt.tight_layout(rect=[0, 0, 1, 0.96])
plt.show()