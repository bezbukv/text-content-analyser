import pyPdf
filename = 'Application Architecture Guide v2.jython_src.pdf'
jython_src.pdf = pyPdf.PdfFileReader(open(filename, "rb"))
for page in jython_src.pdf.pages:
    print page.extractText()[1:30]