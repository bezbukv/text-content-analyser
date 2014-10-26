#-*- coding:utf-8 -*-
import sys
print sys.path
from org.apache.tools.ant import Task
class SimpleTask(Task): 
  message = ""
  def execute(self):
     """@sig public void execute()"""
     Task.log(self, "Message: " + self.message)

  def setMessage(this, aMessage):
     """@sig public void setMessage(java.lang.String str)"""
     this.message = aMessage
