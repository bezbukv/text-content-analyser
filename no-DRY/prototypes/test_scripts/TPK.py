# How it known pyclasspath?
# ['D:\\github-develop\\couples-compiling-and-scripting\\java\\Lib', '__classpath__', '__pyclasspath__/']
from math import sqrt
import sys

def TPK():
  def f(t):
      return sqrt(abs(t))+5*t**3
      
  print "Enter a number at each prompt"
  a = [int(raw_input(">")) for i in range(11)]
  for i in range(10,-1,-1):
      print "for x = ",i,"y ",
      y = f(a[i])
      if y > 400:
          print "IS TOO LARGE"
      else:
          print "= ", y
          
