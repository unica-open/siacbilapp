<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!--modale annulla piano -->
						<div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
							<div class="modal-body">
								<div class="alert alert-error">
									<button type="button" class="close" data-hide="alert">&times;</button>
									<p>
										<strong>Attenzione!</strong>
									</p>
									<p>Stai per annullare il piano di ammortamento: sei sicuro di voler proseguire?</p>
								</div>
							</div>
							<div class="modal-footer">
								<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
								
								<button class="btn btn-primary" formmethod="post" type="submit" formaction="pianoAmmortamentoMutuoTassoVariabile_annullaPiano.do">si, prosegui</button>
							</div>
						</div>

<!--/modale annulla piano -->
