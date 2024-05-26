import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

df = pd.read_csv("../../resources/characteristic-vectors-2605.csv")

sns.lineplot(df["value"])
plt.show()

# plt.plot(range(10))
# plt.savefig("myplot.png")
# plt.show()