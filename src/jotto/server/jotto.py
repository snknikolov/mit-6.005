#!/usr/bin/python

import cgi
import sys
import time
import traceback

def get_puzzle_and_guess():
    form = cgi.FieldStorage()

    if len(form) != 2: return None

    puzzle = form.getlist('puzzle')
    guess = form.getlist('guess') 

    if len(puzzle) != 1 or len(guess) != 1: return None
    return (puzzle[0], guess[0])

def validate_puzzle_id(puzzle):
    try:
        puzzle_int = int(puzzle)
    except ValueError:
        return None

    return puzzle_int if puzzle_int >= 0 else None

def validate_guess(guess, wordlist):
    if len(guess) != 5: return None
    if '*' in guess:
        time.sleep(5)
        return guess
    return guess if guess in wordlist else None

def get_puzzle_word(wordlist, puzzle_id):
    return wordlist[(puzzle_id * 97301) % len(wordlist)]

def get_num_aligned_chars(guess, puzzle):
    """Returns num chars s.t. guess[i] = puzzle[i]"""
    return len([1 for x, y in zip(guess, puzzle) if x == y])

def get_num_matching_chars(guess, puzzle):
    """Returns num chars s.t. guess[i] = puzzle[j], without replacement"""
    return sum([min(guess.count(c), puzzle.count(c)) for c in set(guess)])

def main():
    sys.stdout.write("\n")

    puzzle_and_guess = get_puzzle_and_guess()
    if not puzzle_and_guess:
        sys.stdout.write("error 0\n")
        return

    puzzle_id = validate_puzzle_id(puzzle_and_guess[0])
    if not puzzle_id:
        sys.stdout.write("error 1\n")
        return

    with open("/mit/6.005/web_scripts/wordlist", 'r') as f:
        wordlist = [x.strip() for x in f.readlines()]

    guess = validate_guess(puzzle_and_guess[1], wordlist)
    if not guess:
        sys.stdout.write("error 2\n")
        return

    puzzle = get_puzzle_word(wordlist, puzzle_id)
    sys.stdout.write("guess %d %d\n" % (get_num_matching_chars(guess, puzzle),
                                        get_num_aligned_chars(guess, puzzle)))

if __name__ == "__main__":
    main()
