import pyPdf
filename = 'Application Architecture Guide v2.pdf'
pdf = pyPdf.PdfFileReader(open(filename, "rb"))
for page in pdf.pages:
    print page.extractText()[1:30]