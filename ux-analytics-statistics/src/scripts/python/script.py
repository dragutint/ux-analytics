import pandas as pd
from scipy.stats import ttest_ind
import matplotlib.pyplot as plt
import seaborn as sns

data = pd.read_csv("../../resources/characteristic-vectors-2605.csv")

title_names = {
    'averageMouseSpeed': 'Prosečna Brzina Miša u px/s',
    'averageScrollSpeed': 'Prosečna Brzina Skrolovanja u px/s',
    'correctionRate': 'Stopa Korekcije',
    'formCompletionTime': 'Vreme Popunjavanja u sekundama',
    'idleTime': 'Vreme Neaktivnosti u sekundama',
    'numberOfScrollDirectionChanges': 'Broj Promena Pravca Skrolovanja',
    'totalClicks': 'Ukupan Broj Klikova',
    'totalKeyboardPresses': 'Ukupan Broj Klikova Tastature',
    'totalMouseLength': 'Ukupna Dužina Kretanja Miša u px',
    'totalScrollLength': 'Ukupna Dužina Skrolovanja u px'
}

print(data.head())

characteristics = data['name'].unique()
print(characteristics)

# Function to remove outliers using IQR method
def remove_outliers(data, column_name):
    Q1 = data[column_name].quantile(0.25)
    Q3 = data[column_name].quantile(0.75)
    IQR = Q3 - Q1
    lower_bound = Q1 - 1.5 * IQR
    upper_bound = Q3 + 1.5 * IQR
    return data[(data[column_name] >= lower_bound) & (data[column_name] <= upper_bound)]

# Remove outliers for each characteristic
cleaned_data = pd.DataFrame()
for characteristic in characteristics:
    characteristic_data = data[data['name'] == characteristic]
    cleaned_characteristic_data = remove_outliers(characteristic_data, 'value')
    cleaned_data = pd.concat([cleaned_data, cleaned_characteristic_data])

print(cleaned_data.head())

# Separate data for each form
form1_data = cleaned_data[cleaned_data['form_number'] == 1]
form2_data = cleaned_data[cleaned_data['form_number'] == 2]

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
    sns.boxplot(x='form_number', y='value', data=cleaned_data[cleaned_data['name'] == characteristic], ax=axs[row, col])
    axs[row, col].set_title(title_names[characteristic])
    axs[row, col].set_xlabel('Broj Forme')
    axs[row, col].set_ylabel('Vrednost')

plt.tight_layout(rect=[0, 0, 1, 0.96])
plt.show()