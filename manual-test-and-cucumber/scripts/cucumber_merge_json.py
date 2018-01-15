# coding: utf-8

import fnmatch
import json
import os
import string

class MergeReport():
    """
        Merge several json reports into a single one.
        It's usefull when the same feature is executed with different glues.
    """

    def __init__(self):
        self.features = {}
        self.jsonMerge = []

    def merge_report(self, path, pattern):
        for file in os.listdir(path):

            if fnmatch.fnmatch(file, pattern):
                self.read_json_report('{}/{}'.format(path, file))

        self.write_merge_report()

    def write_merge_report(self):
        with open('reports/cucumber.json', 'w') as outputFile:
            outputFile.write(json.dumps(self.jsonMerge, indent=4, sort_keys=True))


    def read_json_report(self, file_name):
        print('Import: {}'.format(file_name))
        with open(file_name) as f:
            jsonLoad = json.loads(f.read())

        self.merge(jsonLoad)
        print('{} merged'.format(file_name))


    def merge(self, jsonFile):
        for jsonFeature in jsonFile:

            if not jsonFeature['id'] in self.features:
                print('\tAdd: {}'.format(jsonFeature['id']))
                self.features[jsonFeature['id']]=jsonFeature
                self.jsonMerge.append(jsonFeature)
            else:
                print('\tMerge: {}'.format(jsonFeature['id']))
                self.features[jsonFeature['id']]['elements'].extend(jsonFeature['elements'])


if __name__ == '__main__':
    MergeReport().merge_report("reports/cucumber", '*.json')
