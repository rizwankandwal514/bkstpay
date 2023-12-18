import string
import matplotlib.pyplot as plt
from collections import Counter

def word_frequency(text):
    translator = str.maketrans('', '', string.punctuation)
    cleaned_text = text.translate(translator).lower()

    words = cleaned_text.split()
    frequencies = Counter(words)
    return dict(frequencies)

def most_common_words(text, n):
    frequencies = word_frequency(text)
    sorted_words = sorted(frequencies.items(), key=lambda x: (-x[1], x[0]))
    return sorted_words[:n]

def plot_word_frequencies(word_freq):
    words, freqs = zip(*word_freq)
    plt.figure(figsize=(10, 6))
    plt.bar(words, freqs, color='blue')
    plt.xlabel('Words')
    plt.ylabel('Frequencies')
    plt.title('Top 10 Common Words')
    plt.xticks(rotation=45)
    plt.show()

with open("sample_text.txt", "r") as file:
    text = file.read()

    # Calling the functions
    top_words = most_common_words(text, 10)
    print("Top 10 common words:", top_words)
    
    # Plotting the word frequencies
    plot_word_frequencies(top_words)
