<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<!-- accordion per visualizzare il cronoprogramma di gestione aggiunta il 24_03_2015  ahmad  -->

<div class="accordion" id="accordionCronoprogrammaDiGestione">
	<div class="accordion-group">
			<div class="accordion-heading">
				<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordionCronoprogrammaDiGestione" href="#collapseCronoprogrammaDiGestione">
									Cronoprogramma : Gestione esercizio &nbsp; <s:property value="bilancio.anno"/>&nbsp;<span class="icon">&nbsp;</span>
				</a>
			</div>
		<div id="collapseCronoprogrammaDiGestione" class="collapse">
			<div class="accordion-inner">
							
			<h4 class="nostep-pane">
				Prospetto riassuntivo cronoprogramma <span
					id="consultazioneCronoprogrammaDaGestioneCodice"><s:property value="cronoprogrammaDiGestione.codice"/>&nbsp;<s:property value="cronoprogrammaDiGestione.descrizione"/></span>
			</h4>
		
		<div class="modal-body">
			<fieldset class="form-horizontal">
				<table summary="...."
					id="consultazioneCronoprogrammaDaGestioneTabella"
					class="table tab_left">
					<thead>
						<tr>
							<th scope="col">Anno Competenza</th>
							<th scope="col" class="tab_Right">Totale spese</th>
							<th scope="col" class="tab_Right">Totale Entrate Vincolate</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</fieldset>
		</div>
			</div>
		</div>
	</div>
</div>