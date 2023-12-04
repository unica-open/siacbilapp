<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="editStanziamenti" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="overlay-modale">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3 id="titoloModaleVariazioneStanziamenti"></h3>
			</div>
			<div class="modal-body">
				<div class="alert alert-error alert-persistent hide" id="ERRORI_modaleEditStanziamenti">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<strong>Attenzione!!</strong><br>
						<ul>
						</ul>
				</div>
				
				<div id="divComponentiInVariazioneModale" class="hide">
					<div class="accordion_info">
						<div id="headingCollapseComponentiModale" class="accordion-heading">
							<a id="linkCollapseComponentiModale" data-selettore-collapse-interno="#collapseComponentiModale" class="accordion-toggle gestisci-collapse" href="#">
								Componenti <span class="icon"></span>
							</a>
						</div>
							<div id="collapseComponentiModale" class="hide">
								<s:include value="/jsp/variazione/include/fieldsetComponenti.jsp">
									<s:param name="suffix">Modale</s:param>
								</s:include>
							</div>
						</div>
				</div>
				
				<s:include value="/jsp/variazione/include/fieldsetStanziamenti.jsp">
					<s:param name="suffix">Modale</s:param>
				</s:include>
			</div>
			<div class="modal-footer">
				<button type = "button" class="btn" id="button_chiudiRegistraVariazioneModale">chiudi</button>
				<button type = "button" class="btn btn-primary" id="button_registraVariazioneModale">aggiorna modifica</button>
			</div>
		</div>
	</div>
</div>