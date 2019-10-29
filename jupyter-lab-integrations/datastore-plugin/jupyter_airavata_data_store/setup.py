#!/usr/bin/env python
# coding: utf-8

from __future__ import print_function

import os
import sys

from setuptools import find_packages, setup

pjoin = os.path.join
here = os.path.abspath(os.path.dirname(__file__))

# Get the current package version.
version_ns = {}
with open(pjoin(here, 'version.py')) as f:
    exec(f.read(), {}, version_ns)

setup_args = dict(
    name                = 'jupyter_airavata_data_store',
    packages            = find_packages(include=['jupyter_airavata_data_store', 'jupyter_airavata_data_store.*']),
    version             = version_ns['__version__'],
    description         = """Jupyter Airavata Data Store: This overrides default Jupyter filesystem based storage with Airavata storage utilizing Airavata data catalog APIs.""",
    long_description    = "",
    author              = "Dimuthu Wannipurage (https://github.com/DImuthuUpe)",
    author_email        = "dimuthu.upeksha2@gmail.com",
    url                 = "https://github.com/DImuthuUpe/jupyter_airavata_data_store",
    license             = "Apache 2.0",
    platforms           = "Linux, Mac OS X",
    keywords            = ['Data Store', 'Airavata', 'Jupyter'],
    classifiers         = [
        'Intended Audience :: Developers',
        'Intended Audience :: System Administrators',
        'Intended Audience :: Science/Research',
        'License :: Apache 2.0',
        'Programming Language :: Python',
        'Programming Language :: Python :: 3',
    ],
)

# setuptools requirements
if 'setuptools' in sys.modules:
    setup_args['install_requires'] = install_requires = []
    install_requires.append('jupyterhub')
    install_requires.append('lxml')

def main():
    setup(**setup_args)

if __name__ == '__main__':
    main()