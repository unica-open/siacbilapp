<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="accordionDonazioniRinvenimenti" class="accordion">
	<div class="accordion-group">
		<div class="accordion-heading">
				<a id="anchorDonazioniRinvenimenti" href="#collapseDonazioniRinvenimenti" data-parent="#accordionDonazioniRinvenimenti" data-toggle="collapse" class="accordion-toggle collapsed">
				Donazioni/Rinvenimenti<span class="icon"></span>
			</a>
		</div>
		<div class="accordion-body collapse" id="collapseDonazioniRinvenimenti"> 
			<div class="accordion-inner">													
				<fieldset class="form-horizontal hide" id="primaNotaDonazioneRinvenimento">
					<s:iterator value="listaPrimeNote" var="pn">
						<h4 class="step-pane">Elenco scritture prima nota </h4>
						<table class="table table-hover tab_left" id="tabellaScrittureDonazioneRinvenimento">
							<thead>
								<tr>
									<th>Conto</th>
									<th>Descrizione</th>
									<th class="tab_Right">Dare</th>
									<th class="tab_Right">Avere</th>
									<th class="tab_Right span2">&nbsp;</th>
								</tr>
								<s:iterator value="#df.listaMovimentiEP" var="mov">
								
								</s:iterator>
							</thead>
							<tbody>
							</tbody>
						</table>
					</s:iterator>
				</fieldset>	
			</div>
		</div>
	</div>
</div>
									
								