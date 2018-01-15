# coding: utf-8

import json
import string

class Feature():
    def __init__(self, id, name, description):
        self.id = id
        self.name = name
        self.description = description
        self.scenarios = []

    def add_scenarios(self, scenarios):
        self.scenarios.extend(scenarios)

class Scenario():
    def __init__(self, id, name, description, comments, status):
        self.id = id.replace("'", "-")
        self.name = name
        self.description = description
        self.comments = comments
        self.status = status
        self.steps = []

    def add_steps(self, steps):
        self.steps.extend(steps)

class Step():
    def __init__(self, keyword, name, rows, status):
        self.keyword = keyword
        self.name = name
        self.status = status
        self.rows = rows

class Report():

    def __init__(self):
        self.features = {}

    def generate_report(self):
        self.read_json_report('reports/cucumber.json')
        self.write_adoc_report()

    def read_json_report(self, file_name):

        with open(file_name) as f:
            jsonLoad = json.loads(f.read())
            f.closed

            for jsonFeature in jsonLoad:
                self.read_json_feature(jsonFeature)

    def read_json_feature(self, jsonFeature):
        if not jsonFeature['id'] in self.features:
            self.features[jsonFeature['id']] = Feature(jsonFeature['id'], jsonFeature['name'], jsonFeature['description'])

        feature = self.features[jsonFeature['id']]

        feature.add_scenarios(self.get_scenarios(jsonFeature['elements']))

    def get_scenarios(self, json_elements):
        scenarios = []
        for json_scenario in json_elements:
            try:

                scenario = Scenario(json_scenario['id'], json_scenario['name'], json_scenario['description'], json_scenario['comments'] if 'comments' in json_scenario else [], self.get_scenario_status(json_scenario))

                scenario.add_steps(self.get_steps(json_scenario['steps'] if 'steps' in json_scenario else []))
                scenarios.append(scenario)
            except Exception as err:
                print(err)
            #print('Exception with: {}'.format(json_scenario))

        return scenarios

    def get_scenario_status(self, json_scenario):
        if 'before' in json_scenario:
            return json_scenario['before'][0]['result']['status']

        if 'after' in json_scenario:
            return json_scenario['after'][0]['result']['status']

        status = 'passed'
        steps = self.get_steps(json_scenario['steps'] if 'steps' in json_scenario else [])
        for step in steps:
            if step.status == 'failed':
                return 'failed'

            if step.status == 'skipped':
                status = 'skiped'

        return status

    def get_steps(self, json_elements):
        steps = []
        for json_step in json_elements:
            steps.append(Step(json_step['keyword'], json_step['name'],  json_step['rows'] if 'rows' in json_step else [], json_step['result']['status']))

        return steps

    def write_adoc_report(self):
        self.report = open('reports/cucumber.adoc', 'w')
        self.writeTitle('= Cucumber report')
        self.writeLine(':toc: left')
        self.writeLine(':imagesdir: ./images')
        self.writeLine('')

        self.write_summary()

        for id, feature in self.features.items():
            self.write_feature(feature)

        self.report.closed

    #    for id, feature in self.features.items():
    #        self.report = open('report_{}.adoc'.format(id), 'w')
    #        self.write_feature(feature)
    #        self.report.closed

    def write_feature(self, feature):
        self.writeTitle('[#{}]\n==  {}'.format(feature.id, feature.name))
        #self.writeTitle('== {}'.format(feature.name))
        if feature.description != '':
            self.writeLine('[.description]\n{}'.format(feature.description))

        self.write_result(feature);

        #self.writeLine('{}'.format())
        for scenario in feature.scenarios:
            self.writeLine('* <<{},{}>> {}'.format(scenario.id, scenario.name, 'image:icon.{0}.png[{0},20]'.format(scenario.status)))

        for scenario in feature.scenarios:
            self.writeTitle('[#{}]\n===  {}'.format(scenario.id, scenario.name))
            #self.writeLine('[#{}]'.format(scenario.id))
            if scenario.description != '':
                self.writeLine('[.description]\n{}'.format(scenario.description))

            if scenario.comments:
                self.writeLine('\n[.description]')
                self.writeLine('\n....')
            for comment in scenario.comments:
                self.writeLine(comment['value'][2:])
            if scenario.comments:
                self.writeLine('....')
            self.writeLine()
            for step in scenario.steps:
                self.writeLine('* [.keyword]#{}# {} {}'.format(step.keyword.strip(), step.name, 'image:icon.{0}.png[{0},20]'.format(step.status)))
                if step.rows:
                    self.writeLine('\n[.steprows]\n|===')
                    for row in step.rows:
                    #    self.writeLine(cells)
                        self.writeLine('| {}'.format(" | ".join(cell for cell in row['cells'])))
                    self.writeLine('|===\n')

    def write_summary(self):

        self.writeLine('[cols="10,1,1,1,1,1", options="header"]')
        self.writeLine('|====')
        self.writeLine('||Passed|Failed|Pending|Total|%')

        total = {'passed':0, 'failed':0, 'pending':0, 'total':0}
        for id, feature in self.features.items():
            status = self.compute_feature_stat(feature)
            self.writeLine('|<<{},{}>>|{passed}|{failed}|{pending}|{total}|{percentage}'.format(id, feature.name, **status))

            for key, value in total.items():
                total[key] = total[key] + status[key]

        if total['total'] == 0:
            total['percentage'] = '---'
        else:
            total['percentage'] = round(100 * total['passed'] / total['total'])

        self.writeLine('s|{}\ns|{passed}\ns|{failed}\ns|{pending}\ns|{total}\ns|{percentage}'.format('Total', **total))
        self.writeLine('|====')

    def write_result(self, feature):

        status = self.compute_feature_stat(feature)

        self.writeLine('[options="header"]')
        self.writeLine('|====')
        self.writeLine('||Passed|Failed|Pending|Total|%')
        self.writeLine('|Fonctionnalités|{passed}|{failed}|{pending}|{total}|{percentage}'.format(**status))
        self.writeLine('|====')


    def compute_feature_stat(self, feature):
        status = {'passed':0, 'failed':0, 'pending':0, 'total':0}
        for scenario in feature.scenarios:
            status[scenario.status] = 1 + status[scenario.status]
            status['total'] = 1 + status['total']

        status['percentage'] = round(100 * status['passed'] / status['total'])
        return status

    def printElements(self, feature):
        for scenario in feature['elements']:
            self.writeTitle('=== {}'.format(scenario['name']))
            self.writeLine('Result: {}\n'.format(scenario['before'][0]['result']['status']))
            self.writeLine('{}'.format(scenario['description'].strip()))

    def writeTitle(self, message = ''):
        self.writeLine('\n{}'.format(message))

    def writeLine(self, message = ''):
        self.report.write('{}\n'.format(message))

if __name__ == '__main__':
    Report().generate_report()
